import { useMutation, useQueryClient } from "react-query";
import { toast } from "react-toastify";

import {
  addLikeRecruitmentPost,
  addRecruitmentPost,
  applyRecruitmentPost,
  deleteRecruitmentPost,
  modifyRecruitmentPost,
} from "../lib/apis/memberRecruitmentApi";

// 특정 모집글 좋아요
export const useAddLikeRecruitmentMutation = () => {
  const queryClient = useQueryClient();

  return useMutation(addLikeRecruitmentPost, {
    onSuccess: () => {
      queryClient.invalidateQueries("recruitmentDetail");
    },
    onError: (err) => {
      toast.error(err);
    },
  });
};

// 모집글 생성
export const useAddRecruitmentPostMutation = () => {
  const queryClient = useQueryClient();

  return useMutation(addRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글이 생성되었습니다.");
      queryClient.invalidateQueries("allRecruitmentPosts");
    },
  });
};

// 모집글 수정
export const useModifyRecruitmentPostMutation = () => {
  const queryClient = useQueryClient();

  return useMutation(modifyRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글이 수정되었습니다.");
      queryClient.invalidateQueries("recruitmentDetail");
      queryClient.invalidateQueries("allRecruitmentPosts");
    },
  });
};

// 모집글 삭제
export const useDeleteRecruitmentPostMutation = () => {
  const queryClient = useQueryClient();

  return useMutation(deleteRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글이 삭제되었습니다.");
      queryClient.invalidateQueries("allRecruitmentPosts");
    },
  });
};

// 모집글 지원
export const useApplyRecruitmentPostMutation = () => {
  const queryClient = useQueryClient();

  return useMutation(applyRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글 지원하였습니다.");
      queryClient.invalidateQueries("recruitmentDetail");
    },
  });
};
