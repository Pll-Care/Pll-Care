import { useMutation, useQueryClient } from "react-query";
import { toast } from "react-toastify";
import { useNavigate } from "react-router";

import { useRecruitmentClient } from "../../context/Client/RecruitmentClientContext";

// 특정 모집글 좋아요
export const useAddLikeRecruitmentMutation = () => {
  const queryClient = useQueryClient();
  const { addLikeRecruitmentPost } = useRecruitmentClient();

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
  const navigate = useNavigate();
  const { addRecruitmentPost } = useRecruitmentClient();

  return useMutation(addRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글이 생성되었습니다.");
      queryClient.invalidateQueries("allRecruitmentPosts");
      navigate("/recruitment");
    },

    onError: (error) => {
      if (error.response.data.status === 500) {
        toast.error("서버 에러가 발생했습니다. 잠시후에 다시 시도해주세요");
      } else {
        let message;
        message = error.response.data.message;
        toast.error(message);
      }
    },
  });
};

// 모집글 수정
export const useModifyRecruitmentPostMutation = () => {
  const queryClient = useQueryClient();
  const { modifyRecruitmentPost } = useRecruitmentClient();

  return useMutation(modifyRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글이 수정되었습니다.");
      queryClient.invalidateQueries("recruitmentDetail");
      queryClient.invalidateQueries("allRecruitmentPosts");
    },

    onError: (error) => {
      if (error.response.data.status === 500) {
        toast.error("서버 에러가 발생했습니다. 잠시후에 다시 시도해주세요");
      } else {
        let message;
        message = error.response.data.message;
        toast.error(message);
      }
    },
  });
};

// 모집글 삭제
export const useDeleteRecruitmentPostMutation = () => {
  const queryClient = useQueryClient();
  const { deleteRecruitmentPost } = useRecruitmentClient();

  return useMutation(deleteRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글이 삭제되었습니다.");
      queryClient.invalidateQueries("allRecruitmentPosts");
    },

    onError: (error) => {
      if (error.response.data.status === 500) {
        toast.error("서버 에러가 발생했습니다. 잠시후에 다시 시도해주세요");
      } else {
        let message;
        message = error.response.data.message;
        toast.error(message);
      }
    },
  });
};

// 모집글 지원
export const useApplyRecruitmentPostMutation = () => {
  const queryClient = useQueryClient();
  const { applyRecruitmentPost } = useRecruitmentClient();

  return useMutation(applyRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글 지원하였습니다.");
      queryClient.invalidateQueries("recruitmentDetail");
    },

    onError: (error) => {
      if (error.response.data.status === 500) {
        toast.error("서버 에러가 발생했습니다. 잠시후에 다시 시도해주세요");
      } else {
        let message;
        message = error.response.data.message;
        toast.error(message);
      }
    },
  });
};

// 모집글 지원 취소
export const useApplyCancelRecruitmentPostMutation = () => {
  const queryClient = useQueryClient();
  const { applyCancelRecruitmentPost } = useRecruitmentClient();

  return useMutation(applyCancelRecruitmentPost, {
    onSuccess: () => {
      toast.success("모집글 지원취소하였습니다.");
      queryClient.invalidateQueries("recruitmentDetail");
    },

    onError: (error) => {
      if (error.response.data.status === 500) {
        toast.error("서버 에러가 발생했습니다. 잠시후에 다시 시도해주세요");
      } else {
        let message;
        message = error.response.data.message;
        toast.error(message);
      }
    },
  });
};
