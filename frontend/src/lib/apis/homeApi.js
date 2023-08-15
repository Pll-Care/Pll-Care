import { customAxios } from "../apis/customAxios";

export const getPopularProjects = async () => {
  const response = await customAxios.get("/auth/main/mostliked");

  return response.data;
};

export const getImminentProjects = async () => {
  const response = await customAxios.get("/auth/main/closedeadline");

  return response.data;
};

export const getUpToDateProjects = async () => {
  const response = await customAxios.get("/auth/main/uptodate");

  return response.data;
};
