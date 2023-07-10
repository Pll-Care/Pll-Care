import { customAxios } from "./customAxios";

// 페이지네이션 모집글 조회하는 함수
//
export const getAllRecruitmentPost = async (page) => {
  try {
    const res = await customAxios.get(
      `/auth/post/list?page=1&size=${page}&direction=ASC`
    );
    return res.data;
  } catch (err) {
    return err;
  }
};
