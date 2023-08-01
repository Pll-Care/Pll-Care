import { useMutation, useQueryClient } from "react-query";
import { useDispatch, useSelector } from "react-redux";

import { createFinalEvaluation } from "../lib/apis/evaluationManagementApi";

import { toast } from "react-toastify";

const useEvaluationManagementMutation = () => {
  const queryClient = useQueryClient();
  const dispatch = useDispatch();

  const { mutate: finalEvaluationMutate } = useMutation(createFinalEvaluation, {
    onSuccess: () => {
      toast.success("최종 평가가 완료되었습니다!");
      queryClient.invalidateQueries([
        "managementFinalEvaluationChartAndRanking",
      ]);
      queryClient.invalidateQueries([
        "managementEvaluationAllParticipants",
      ]);
    },
    onError: () => {
      toast.error("최종 평가 실패했습니다. 잠시 후 다시 시도해주세요.");
    },
  });

  return { finalEvaluationMutate };
};

export default useEvaluationManagementMutation;
