import { useMutation, useQueryClient } from "react-query";
import { toast } from "react-toastify";

import { addLikeRecruitmentPost } from "../lib/apis/memberRecruitmentApi";

// 특정 모집글 좋아요
export const useAddLikeRecruitmentMutation = () => {
  const queryClient = useQueryClient();

  return useMutation(addLikeRecruitmentPost, {
    onSuccess: (data) => {
      console.log(data);
      queryClient.invalidateQueries("recruitmentDetail");
    },
    onError: (err) => {
      toast.error(err);
    },
  });
};
