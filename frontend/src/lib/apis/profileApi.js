/**
 * 유저 아이디 조회 - end
 * 내 프폴필 검증 - end
 
 * 한줄 소개 조회 - end
 * 한줄 소개 수정 - end

 * 연락처 조회  - end
 * 연락처 수정  - end

 * 직무 기술스택 조회
 * 기술 스택 검색
 * 직무 기술스택 수정
 * 
 
 * 프로젝트 경험 조회

 # 개인 프로필 생성 수정 삭제


 */

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

export const putBio = async (memberId, bio) => {
  try {
    return await customAxios.put(`/auth/profile/${memberId}`, {
      bio,
    });
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
