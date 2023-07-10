import { customAxios } from "./customAxios";

// 페이지네이션 모집글 조회하는 함수
export const getAllRecruitmentPost = async (page, size) => {
  try {
    const res = await customAxios.get(
      `/auth/post/list?page=${page}&size=${size}&direction=ASC`
    );
    return res.data;
  } catch (err) {
    return err;
  }
};

// 모집글 디테일 조회하는 함수
export const getRecruitmentPostDetail = async (postId) => {
  try {
    const res = await customAxios.get(`/auth/post/${postId}`);
    return res.data;
  } catch (err) {
    return err;
  }
};
