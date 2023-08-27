import { customAxios } from "./customAxios";
import { toast } from "react-toastify";

export const deleteProject = async (projectId) => {
  try {
    const response = await customAxios.delete(`/auth/project/${projectId}`);

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
};

export const completeProject = async (projectId) => {
  try {
    const response = await customAxios.post(`/auth/project/${projectId}/complete`, {
      state: "COMPLETE",
    });

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
};

export const getProjectData = async (projectId) => {
  try {
    const response = await customAxios.get(`/auth/project/${projectId}`);

    return response.data;
  } catch (e) {
    if (e.response.data.code === "PROJECT_001") {
      toast.error("프로젝트를 찾을 수 없습니다.");
    } else if (e.response.data.code === "PROJECT_004") {
      toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
    }
  }
};
