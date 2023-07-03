import { customAxios } from "./customAxios";

export const getTeamMember = async (project_id) => {
  try {
    const response = await customAxios.get(
      `/auth/project/${project_id}/memberlist`
    );

    if (response.status === 200) return response.data;
  } catch (error) {
    console.log(error);
  }
};

export const deleteMember = async (projectId, memberId) => {
  try {
    return await customAxios.post("/auth/project/memberout", {
      projectId,
      memberId,
    });
  } catch (error) {
    return {
      status: error.response.status,
      message: error.response.data.message,
    };
  }
};

export const getApplicationUser = async (projectId) => {
  try {
    const response = await customAxios.get(
      `/auth/project/${projectId}/applylist`
    );

    if (response.status === 200) return response.data;
  } catch (error) {
    console.log(error);
  }
};
