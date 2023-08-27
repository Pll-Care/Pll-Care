import { customAxios } from "./customAxios";
import { toast } from "react-toastify";

export const getAllMeetingRecordList = async (
  projectId,
  pageNum = 1,
  direction = "DESC"
) => {
  try {
    const response = await customAxios.get(
      `/auth/memo/list?project_id=${projectId}&page=${pageNum}&size=2&direction=${direction}`
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
};

export const getBookMarkMeetingRecordList = async (projectId, pageNum = 1) => {
  try {
    const response = await customAxios.get(
      `/auth/memo/bookmarklist?project_id=${projectId}&page=${pageNum}&size=2`
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
};

export const createMeetingRecord = async (newMeetingRecordObj) => {
  try {
    const response = await customAxios.post(`/auth/memo`, newMeetingRecordObj);

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
};

export const getMeetingRecord = async (meetingRecordId, projectId) => {
  try {
    if (meetingRecordId !== -1) {
      const response = await customAxios.get(
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
};

export const deleteMeetingRecord = async (newMeetingRecordObj) => {
  try {
    const response = await customAxios.delete(
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
};

export const createBookMarkMeetingRecord = async (newMeetingRecordObj) => {
  try {
    const response = await customAxios.post(
      `/auth/memo/${newMeetingRecordObj.meetingRecordId}/bookmark`,
      {
        projectId: newMeetingRecordObj.projectId,
      }
    );

    toast.success("북마크 / 북마크 취소되었습니다!");

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
};

export const editMeetingRecord = async (newMeetingRecordObj) => {
  try {
    const response = await customAxios.put(
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
    } else if (e.response.data.code === "MEMO_008") {
      toast.error("완료된 프로젝트는 회의록을 수정할 수 없습니다.");
    }
  }
};
