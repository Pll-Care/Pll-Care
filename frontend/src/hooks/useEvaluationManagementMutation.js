import { useMutation, useQueryClient } from "react-query";

import { createFinalEvaluation } from "../lib/apis/evaluationManagementApi";

const useEvaluationManagementMutation = () => {
  const queryClient = useQueryClient();

  const { mutate: finalEvaluationMutate } = useMutation(createFinalEvaluation, {
    onSuccess: () => {
      queryClient.invalidateQueries([
        "managementFinalEvaluationChartAndRanking",
      ]);
      queryClient.invalidateQueries([
        "managementEvaluationAllParticipants",
      ]);
    }
  });

  return { finalEvaluationMutate };
};

export default useEvaluationManagementMutation;
