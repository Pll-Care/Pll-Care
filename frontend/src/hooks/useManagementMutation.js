import { useMutation, useQueryClient } from "react-query";
import { completeProject, createProject, deleteProject } from "../lib/apis/projectManagementApi";
import { toast } from "react-toastify";

const useManagementMutation = () => {
  const queryClient = useQueryClient();

  const { mutate: createMutate } = useMutation(createProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
      toast.success("생성되었습니다!");
    },
  });

  const { mutate: deleteMutate } = useMutation(deleteProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
      toast.success("삭제되었습니다!");
    },
  });

  const { mutate: completeMutate } = useMutation(completeProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
      toast.success("완료 처리되었습니다!");
    }
  })

  return { createMutate, deleteMutate, completeMutate };
};

export default useManagementMutation;