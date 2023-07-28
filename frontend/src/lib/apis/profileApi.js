import { customAxios } from "./customAxios";

export const getMemberId = async () => {
  try {
    const response = await customAxios.get("/auth/member/onlyid");
    if (response.status === 200) return response.data.id;
  } catch (error) {
    console.log(error);
  }
};

export const validateProfile = async (memberId) => {
  try {
    const response = await customAxios.get(
      `/auth/profile/${memberId}/validate`
    );
    if (response.status === 200) return response.data.myProfile;
  } catch (error) {
    console.log(error);
  }
};

export const getBio = async (memberId) => {
  try {
    const response = await customAxios.get(`/auth/profile/${memberId}/bio`);
    if (response.status === 200) return response.data;
  } catch (error) {
    console.log(error);
  }
};

//bio: string
//nickname: string
//imageUrl: string

export const putBioAPI = async (memberId, reqBody) => {
  try {
    return await customAxios.put(`/auth/profile/${memberId}`, reqBody);
  } catch (error) {
    console.log(error);
  }
};

export const getContact = async (memberId) => {
  try {
    const response = await customAxios.get(`/auth/profile/${memberId}/contact`);
    if (response.status === 200) return response.data;
  } catch (error) {
    console.log(error);
  }
};

export const patchProfile = async (memberId, userInfo) => {
  try {
    await customAxios.patch(`/auth/profile/${memberId}`, userInfo);
  } catch (error) {
    console.log(error);
  }
};

export const getPositionAPI = async (memberId) => {
  try {
    const response = await customAxios.get(
      `/auth/profile/${memberId}/roletechstack`
    );

    if (response.status === 200) return response.data;
  } catch (error) {
    console.log(error);
  }
};

export const searchTechAPI = async (tech) => {
  try {
    const response = await customAxios.get(`/auth/util/techstack?tech=${tech}`);
    if (response) return response.data;
  } catch (error) {
    console.log(error);
  }
};

export const getProjectExperienceAPI = async (memberId) => {
  try {
    const response = await customAxios.get(
      `/auth/profile/${memberId}/experience`
    );
    console.log(response);
  } catch (error) {
    console.log(error);
  }
};

export const getEvaluationChartAPI = (memberId) => {
  return customAxios.get(`/auth/profile/${memberId}/evaluation/chart`);
};

// page: number
export const getEvaluationProjectListAPI = (memberId, page) => {
  return customAxios.get(`/auth/profile/${memberId}/evaluation?page=${page}`);
};

// projectId: number
export const getEvaluationProjectDetailAPI = async (memberId, projectId) => {
  try {
    const response = await customAxios.get(
      `/auth/profile/${memberId}/evaluation/${projectId}`
    );

    if (response.status === 200) return response.data;
  } catch (error) {
    console.log(error);
  }
};

//state: string
//page:number
export const getPostProjectAPI = async (memberId, state, page) => {
  try {
    const response = await customAxios.get(
      `/auth/profile/${memberId}/post?state=${state}&page=${page}`
    );
    if (response.status === 200) return response.data;
  } catch (error) {
    console.log(error);
  }
};

export const getLikeProjectAPI = async (memberId, state, page) => {
  try {
    const response = await customAxios.get(
      `/auth/profile/${memberId}/post/like?page=${page}`
    );

    if (response.status === 200) return response.data;
  } catch (error) {
    console.log(error);
  }
};
