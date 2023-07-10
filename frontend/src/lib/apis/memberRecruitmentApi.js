import { useMutation, useQueryClient } from "react-query";
import { customAxios } from "./customAxios";
import { toast } from "react-toastify";

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

// 특정 모집글 좋아요
export const addLikeRecruitmentPost = async (postId) => {
  try {
    const res = await customAxios.post(`/auth/post/${postId}/like`);
    return res.data;
  } catch (err) {
    return err;
  }
};
