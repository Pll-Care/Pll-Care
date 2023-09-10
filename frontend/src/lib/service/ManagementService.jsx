import { toast } from "react-toastify";

class ManagementService {
  httpClient = null;

  constructor(httpClient) {
    this.httpClient = httpClient;
  }

  // managementApi
  async getProjectList(pageNum = 1, state) {
    let response = null;

    try {
      if (state === "ALL") {
        response = await this.httpClient.get(
          `/auth/project/list?page=${pageNum}&size=4&state=ONGOING&state=COMPLETE`
        );
      } else {
        response = await this.httpClient.get(
          `/auth/project/list?page=${pageNum}&size=4&state=${state}`
        );
      }

      return {
        projectList: response.data.content,
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
      };
    } catch (e) {
      console.log(e);
    }
  }

  async createProject(newProjectObj) {
    try {
      const response = await this.httpClient.post(
        "/auth/project",
        newProjectObj
      );

      toast.success("생성되었습니다!");

      return response.data.content;
    } catch (e) {
      if (e.response.data.code === "MEMBER_001") {
        toast.error("사용자를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_003")
        toast.error("시작일자와 종료일자가 올바르지 않습니다.");
    }
  }

  async leaveProject(projectId) {
    try {
      const response = await this.httpClient.delete(
        `/auth/project/${projectId}/selfout`
      );

      toast.success("탈퇴되었습니다!");

      return response.data;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      } else if (e.response.data.code === "PROJECT_011") {
        toast.error("완료된 프로젝트에서는 해당 기능을 사용할  수 없습니다.");
      } else if (e.response.data.code === "PROJECT_012") {
        toast.error("리더를 위임하고 탈퇴하세요.");
      }
    }
  }

  async uploadImage(imgData) {
    try {
      const response = await this.httpClient.post(
        `/auth/upload/image?dir=${imgData.dir}`,
        {
          file: imgData.formData,
        },
        {
          headers: { "Content-Type": "multipart/form-data" },
        }
      );

      return response.data.imageUrl;
    } catch (e) {
      if (e.response.data.code === "AWS_002") {
        toast.error("파일 업로드를 실패했습니다.");
      }
    }
  }

  async deleteImage(imgUrl) {
    try {
      const response = await this.httpClient.delete(
        `/auth/upload/image?url=${imgUrl}`
      );

      return response.data;
    } catch (e) {
      if (e.response.data.code === "AWS_001") {
        toast.error("파일을 찾을 수 없습니다.");
      }
    }
  }

  async getCompleteProjectData(projectId) {
    try {
      const response = await this.httpClient.get(
        `/auth/project/${projectId}/iscompleted`
      );

      return response.data.completed;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      }
    }
  }

  async getIsLeaderData(projectId) {
    try {
      const response = await this.httpClient.get(
        `/auth/project/${projectId}/isleader`
      );

      return response.data.leader;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      }
    }
  }

  // evaluationManagementApi
  async getMidEvaluationChartAndRanking(projectId) {
    try {
      const response = await this.httpClient.get(
        `/auth/evaluation/midtermlist?project_id=${projectId}`
      );

      return response.data;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      }
    }
  }

  async getFinalEvaluationChartAndRanking(projectId) {
    try {
      const response = await this.httpClient.get(
        `/auth/evaluation/finallist?project_id=${projectId}`
      );

      return response.data;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      }
    }
  }

  async getEvaluationMember(projectId) {
    try {
      const response = await this.httpClient.get(
        `/auth/evaluation/participant?project_id=${projectId}`
      );

      return response.data;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      }
    }
  }

  async createFinalEvaluation(finalEvaluationObj) {
    try {
      const response = await this.httpClient.post(
        `/auth/evaluation/final`,
        finalEvaluationObj
      );

      toast.success("최종 평가되었습니다!");

      return response;
    } catch (e) {
      if (e.response.data.code === "MEMBER_001") {
        toast.error("사용자를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_002") {
        toast.error("프로젝트에서 해당 사용자를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_015") {
        toast.error("완료되지 않은 프로젝트입니다.");
      }
      if (e.response.data.code === "EVAL_002") {
        toast.error("최종 평가 점수가 범위를 벗어났습니다.");
      } else if (e.response.data.code === "EVAL_006") {
        toast.error("자기 자신의 평가를 할 수 없습니다.");
      } else if (e.response.data.code === "EVAL_007") {
        toast.error("동일한 평가를 다시 할 수 없습니다.");
      }
    }
  }

  async getFinalEvaluation(finalEvalId, projectId) {
    try {
      const response = await this.httpClient.get(
        `/auth/evaluation/final/${finalEvalId}?project_id=${projectId}`
      );

      return response.data;
    } catch (e) {
      if (e.response.data.code === "EVAL_001") {
        toast.error("평가를 찾을 수 없습니다.");
      }
    }
  }

  // meetingRecordManagementApi
  async getAllMeetingRecordList(projectId, pageNum = 1, direction = "DESC") {
    try {
      const response = await this.httpClient.get(
        `/auth/memo/list?project_id=${projectId}&page=${pageNum}&size=3&direction=${direction}`
      );

      return {
        meetingRecordList: response.data.content,
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
      };
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      }
    }
  }

  async getBookMarkMeetingRecordList(projectId, pageNum = 1) {
    try {
      const response = await this.httpClient.get(
        `/auth/memo/bookmarklist?project_id=${projectId}&page=${pageNum}&size=3`
      );

      return {
        meetingRecordList: response.data.content,
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
      };
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      }
    }
  }

  async createMeetingRecord(newMeetingRecordObj) {
    try {
      const response = await this.httpClient.post(
        `/auth/memo`,
        newMeetingRecordObj
      );

      toast.success("생성되었습니다!");

      return response.data.memoId;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      } else if (e.response.data.code === "MEMO_007") {
        toast.error("완료된 프로젝트는 회의록을 생성할 수 없습니다.");
      }
    }
  }

  async getMeetingRecord(meetingRecordId, projectId) {
    try {
      if (meetingRecordId !== -1) {
        const response = await this.httpClient.get(
          `/auth/memo/${meetingRecordId}?project_id=${projectId}`
        );

        return response.data;
      }
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      } else if (e.response.data.code === "MEMO_001") {
        toast.error("회의록을 찾을 수 없습니다.");
      }
    }
  }

  async deleteMeetingRecord(newMeetingRecordObj) {
    try {
      const response = await this.httpClient.delete(
        `/auth/memo/${newMeetingRecordObj.meetingRecordId}`,
        {
          data: { projectId: newMeetingRecordObj.projectId },
        }
      );

      toast.success("삭제되었습니다!");

      return response.data;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      } else if (e.response.data.code === "MEMO_007") {
        toast.error("완료된 프로젝트는 회의록을 생성할 수 없습니다.");
      }
    }
  }

  async createBookMarkMeetingRecord(newMeetingRecordObj) {
    try {
      const response = await this.httpClient.post(
        `/auth/memo/${newMeetingRecordObj.meetingRecordId}/bookmark`,
        {
          projectId: newMeetingRecordObj.projectId,
        }
      );

      newMeetingRecordObj.type === "cancel"
        ? toast.success("북마크 취소되었습니다!")
        : toast.success("북마크되었습니다!");

      return response.data;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      } else if (e.response.data.code === "MEMO_010") {
        toast.error("완료된 프로젝트는 회의록을 북마크할 수 없습니다.");
      }
    }
  }

  async editMeetingRecord(newMeetingRecordObj) {
    try {
      const response = await this.httpClient.put(
        `/auth/memo/${newMeetingRecordObj.selectedMeetingRecordId}`,
        {
          projectId: newMeetingRecordObj.projectId,
          title: newMeetingRecordObj.title,
          content: newMeetingRecordObj.content,
        }
      );

      toast.success("수정되었습니다!");

      return response.data.memoId;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      } else if (e.response.data.code === "MEMO_010") {
        toast.error("완료된 프로젝트는 회의록을 수정할 수 없습니다.");
      }
    }
  }

  // projectManagementApi
  async deleteProject(projectId) {
    try {
      const response = await this.httpClient.delete(
        `/auth/project/${projectId}`
      );

      toast.success("삭제되었습니다!");

      return response.data.content;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      } else if (e.response.data.code === "PROJECT_010") {
        toast.error("완료된 프로젝트는 삭제할 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_006") {
        toast.error("해당 프로젝트에 대한 삭제 권한이 없습니다.");
      }
    }
  }

  async editProject(newProjectData) {
    try {
      const response = await this.httpClient.put(
        `/auth/project/${newProjectData.projectId}`,
        {
          title: newProjectData.title,
          description: newProjectData.description,
          state: newProjectData.state,
          startDate: newProjectData.startDate,
          endDate: newProjectData.endDate,
          imageUrl: newProjectData.imageUrl,
        }
      );

      toast.success("수정되었습니다!");

      return response.data;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      } else if (e.response.data.code === "PROJECT_009") {
        toast.error("완료된 프로젝트는 수정할 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_005") {
        toast.error("해당 프로젝트에 대한 수정 권한이 없습니다.");
      } else if (e.response.data.code === "PROJECT_003") {
        toast.error("시작일자와 종료일자가 올바르지 않습니다.");
      }
    }
  }

  async completeProject(projectId) {
    try {
      const response = await this.httpClient.post(
        `/auth/project/${projectId}/complete`,
        {
          state: "COMPLETE",
        }
      );

      toast.success("완료 처리되었습니다!");

      return response.data;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      } else if (e.response.data.code === "PROJECT_007") {
        toast.error("해당 프로젝트에 대한 완료 권한이 없습니다.");
      } else if (e.response.data.code === "PROJECT_014") {
        toast.error("이미 완료된 프로젝트입니다.");
      }
    }
  }

  async getProjectData(projectId) {
    try {
      const response = await this.httpClient.get(`/auth/project/${projectId}`);

      return response.data;
    } catch (e) {
      if (e.response.data.code === "PROJECT_001") {
        toast.error("프로젝트를 찾을 수 없습니다.");
      } else if (e.response.data.code === "PROJECT_004") {
        toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
      }
    }
  }
}

export default ManagementService;
