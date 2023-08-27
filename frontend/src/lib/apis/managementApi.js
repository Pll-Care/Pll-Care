import { customAxios } from "./customAxios";
import { toast } from "react-toastify";

export const getProjectList = async (pageNum = 1, state) => {
  let response = null;

  try {
    if (state === "ALL") {
      response = await customAxios.get(
        `/auth/project/list?page=${pageNum}&size=4&state=ONGOING&state=COMPLETE`
      );
    } else {
      response = await customAxios.get(
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
};

export const createProject = async (newProjectObj) => {
  try {
    const response = await customAxios.post("/auth/project", newProjectObj);

    toast.success("생성되었습니다!");

    return response.data.content;
  } catch (e) {
    if (e.response.data.code === "MEMBER_001") {
      toast.error("사용자를 찾을 수 없습니다.");
    } else if (e.response.data.code === "PROJECT_003") {
      toast.error("시작일자와 종료일자가 올바르지 않습니다.");
    }
  }
};

export const leaveProject = async (projectId) => {
  try {
    const response = await customAxios.delete(
      `/auth/project/${projectId}/selfout`
    );

    toast.success("탈퇴되었습니다!");

    return response.data;
  } catch (e) {
    if (e.response.data.code === "PROJECT_001") {
      toast.error("프로젝트를 찾을 수 없습니다.");
    } else if (e.response.data.code === "PROJECT_004") {
      toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
    } else if (e.response.data.code === "PROJECT_012") {
      toast.error("프로젝트 리더는 사용할 수 없는 기능입니다. 리더를 위임하고 탈퇴하세요.");
    } else if (e.response.data.code === "PROJECT_011") {
      toast.error("완료된 프로젝트에서는 해당 기능을 사용할  수 없습니다.");
    }
  }
};

export const uploadImage = async (imgData) => {
  try {
    const response = await customAxios.post(
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
};

export const deleteImage = async (imgUrl) => {
  try {
    const response = await customAxios.delete(
      `/auth/upload/image?url=${imgUrl}`
    );

    return response.data;
  } catch (e) {
    if (e.response.data.code === "AWS_001") {
      toast.error("파일을 찾을 수 없습니다.");
    }
  }
};

export const getCompleteProjectData = async (projectId) => {
  try {
    const response = await customAxios.get(
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
};

export const getIsLeaderData = async (projectId) => {
  try {
    const response = await customAxios.get(
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
};
