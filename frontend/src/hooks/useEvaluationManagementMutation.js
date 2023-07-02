import { useMutation } from "react-query";
import { createFinalEvaluation } from "../lib/apis/evaluationManagementApi";
import { toast } from "react-toastify";

const useEvaluationManagementMutation = () => {
  const { mutate: finalEvaluationMutate } = useMutation(createFinalEvaluation, {
    onSuccess: () => {
      toast("최종 평가가 완료되었습니다!");
    },
    onError: () => {
      toast.error("최종 평가 실패했습니다. 잠시 후 다시 시도해주세요.");
    }
  });

  return { finalEvaluationMutate };
};

export default useEvaluationManagementMutation;
