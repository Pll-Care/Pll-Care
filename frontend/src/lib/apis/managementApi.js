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
    if (
      e.response.data.code === "MEMBER_001" ||
      e.response.data.code === "PROJECT_003"
    ) {
      toast.error(e.response.data.message);
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
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004" ||
      e.response.data.code === "PROJECT_011"
    ) {
      toast.error(e.response.data.message);
    } else if (e.response.data.code === "PROJECT_012") {
      toast.error(
        "리더를 위임하고 탈퇴하세요."
      );
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
      toast.error(e.response.data.message);
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
      toast.error(e.response.data.message);
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
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004"
    ) {
      toast.error(e.response.data.message);
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
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004"
    ) {
      toast.error(e.response.data.message);
    }
  }
};
