import { useMutation, useQueryClient } from "react-query";

import { useManagementClient } from "../../context/Client/ManagementClientContext";

const useEvaluationManagementMutation = () => {
  const queryClient = useQueryClient();

  const { createFinalEvaluation } = useManagementClient();

  const { mutate: finalEvaluationMutate } = useMutation(createFinalEvaluation, {
    onSuccess: () => {
      queryClient.invalidateQueries([
        "managementFinalEvaluationChartAndRanking",
      ]);
      queryClient.invalidateQueries(["managementEvaluationAllParticipants"]);
    },
  });

  return { finalEvaluationMutate };
};

export default useEvaluationManagementMutation;
