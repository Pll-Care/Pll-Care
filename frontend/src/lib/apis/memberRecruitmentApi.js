import { useNavigate } from "react-router";
import { customAxios } from "./customAxios";

// 키값 : allRecruitmentPosts
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

// 키값 : recruitmentDetail
// 모집글 디테일 조회하는 함수
export const getRecruitmentPostDetail = async (postId) => {
  try {
    const res = await customAxios.get(`/auth/post/${postId}`);
    return res.data;
  } catch (error) {
    throw error;
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

// 모집글 작성할 프로젝트 조회
export const getRecruitmentProject = async () => {
  try {
    const res = await customAxios.get("/auth/project/simplelist");
    return res.data.data;
  } catch (err) {
    return err;
  }
};

// 모집글 생성
export const addRecruitmentPost = async (body) => {
  try {
    const res = await customAxios.post("/auth/post", body);
    return res.data;
  } catch (err) {
    return err;
  }
};

// 모집글 수정
export const modifyRecruitmentPost = async (body) => {
  const { postId, ...formData } = body;
  try {
    const res = await customAxios.put(`/auth/post/${postId}`, formData);
    return res.data;
  } catch (err) {
    return err;
  }
};

// 모집글 삭제
export const deleteRecruitmentPost = async (postId) => {
  try {
    const res = await customAxios.delete(`/auth/post/${postId}`);
    return res.data;
  } catch (err) {
    return err;
  }
};

// 모집글 지원
export const applyRecruitmentPost = async (body) => {
  const { postId, position } = body;
  const res = await customAxios.post(`/auth/post/${postId}/apply`, {
    position: position,
  });
  return res;
};
