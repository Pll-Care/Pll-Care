import { useMutation, useQueryClient } from "react-query";
import { completeProject, createProject, deleteProject, editProject } from "../lib/apis/projectManagementApi";
import { toast } from "react-toastify";
import { useDispatch } from "react-redux";

import {projectManagementActions } from "../redux/projectManagementSlice";

const useManagementMutation = () => {
  const queryClient = useQueryClient();

  const dispatch = useDispatch();

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
    onSuccess: (data) => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);

      dispatch(projectManagementActions.addCompletedProjectId(data.projectId));

      toast.success("완료 처리되었습니다!");
    }
  })

  const { mutate: editMutate } = useMutation(editProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
      toast.success("수정되었습니다!");
    }
  })

  return { createMutate, deleteMutate, completeMutate, editMutate };
};

export default useManagementMutation;