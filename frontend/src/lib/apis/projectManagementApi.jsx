import { customAxios } from "./customAxios";
import { toast } from "react-toastify";

export const deleteProject = async (projectId) => {
  try {
    const response = await customAxios.delete(`/auth/project/${projectId}`);

    toast.success("삭제되었습니다!");

    return response.data.content;
  } catch (e) {
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004" ||
      e.response.data.code === "PROJECT_010" ||
      e.response.data.code === "PROJECT_006"
    ) {
      toast.error(e.response.data.message);
    }
  }
};

export const editProject = async (newProjectData) => {
  try {
    const response = await customAxios.put(
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
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004" ||
      e.response.data.code === "PROJECT_009" ||
      e.response.data.code === "PROJECT_005" ||
      e.response.data.code === "PROJECT_003"
    ) {
      toast.error(e.response.data.message);
    }
  }
};

export const completeProject = async (projectId) => {
  try {
    const response = await customAxios.post(
      `/auth/project/${projectId}/complete`,
      {
        state: "COMPLETE",
      }
    );

    toast.success("완료 처리되었습니다!");

    return response.data;
  } catch (e) {
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004" ||
      e.response.data.code === "PROJECT_007" ||
      e.response.data.code === "PROJECT_014"
    ) {
      toast.error(e.response.data.message);
    }
  }
};

export const getProjectData = async (projectId) => {
  try {
    const response = await customAxios.get(`/auth/project/${projectId}`);

    return response.data;
  } catch (e) {
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004"
    ) {
      toast.error(e.response.data.message);
    }
  }
};
