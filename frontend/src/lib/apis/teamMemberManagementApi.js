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

export const postApplyAcceptAPI = (projectId, memberId, postId) => {
  return customAxios.post(`/auth/project/${projectId}/applyaccept`, {
    memberId,
    postId,
  });
};
export const postApplyRejectAPI = (projectId, memberId, postId) => {
  return customAxios.post(`/auth/project/${projectId}/applyreject`, {
    memberId,
    postId,
  });
};

export const putPositionChangeAPI = (projectId, memberId, position) => {
  return customAxios.put(`/auth/project/${projectId}/positionchange`, {
    memberId,
    position,
  });
};

export const putLeaderChangeAPI = (projectId, memberId) => {
  return customAxios.put(`/auth/project/${projectId}/leaderchange`, {
    memberId,
  });
};

export const deleteKickoutAPI = (projectId, memberId) => {
  return customAxios.delete(`/auth/project/${projectId}/kickout`, {
    data: {
      memberId,
    },
  });
};
