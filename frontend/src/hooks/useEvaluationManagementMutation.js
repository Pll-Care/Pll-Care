import { useMutation } from "react-query";
import { createFinalEvaluation } from "../lib/apis/evaluationManagementApi";
import { toast } from "react-toastify";

const useEvaluationManagementMutation = () => {
  const { mutate: finalEvaluationMutate } = useMutation(createFinalEvaluation, {
    onSuccess: () => {
      toast("최종 평가가 완료되었습니다!");
    },
  });

  return { finalEvaluationMutate };
};

export default useEvaluationManagementMutation;
