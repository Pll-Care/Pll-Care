import { useMutation, useQueryClient } from "react-query";
import { createProject, deleteProject } from "../lib/apis/projectManagementApi";
import { toast } from "react-toastify";

const useManagementMutation = () => {
  const queryClient = useQueryClient();

  const { mutate: createMutate } = useMutation(createProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementProjectList"]);
      toast.success("생성되었습니다!");
    },
  });

  const { mutate: deleteMutate } = useMutation(deleteProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementProjectList"]);
      toast.success("삭제되었습니다!");
    },
  });

  return { createMutate, deleteMutate };
};

export default useManagementMutation;