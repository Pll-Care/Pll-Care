import { customAxios } from "./customAxios";

export const getProjectData = async (projectId) => {
  const response = await customAxios.get(`/auth/project/${projectId}`);

  return response.data;
};
