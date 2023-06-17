import { useMutation, useQueryClient } from "react-query";
import {
  createProject,
  deleteProject,
} from "../../lib/apis/projectManagementApi";

const useManagementMutation = () => {
  const queryClient = useQueryClient();

  const { mutate: createMutate } = useMutation(createProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementProjectList"]);
    },
  });

  const { mutate: deleteMutate } = useMutation(deleteProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementProjectList"]);
    },
  });

  return { createMutate, deleteMutate };
};

export default useManagementMutation;
