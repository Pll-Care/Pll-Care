import { useNavigate } from "react-router-dom";
import { useQueryClient, useMutation } from "react-query";

import { toast } from "react-toastify";

import {
  completeProject,
  createProject,
  deleteProject,
  editProject,
  leaveProject,
} from "../lib/apis/managementApi";

const useManagementMutation = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const { mutate: createMutate } = useMutation(createProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
      toast.success("생성되었습니다!");
    },
    onError: () => {
      toast.error("생성 실패하였습니다. 잠시 후 다시 시도해주세요.");
    },
  });

  const { mutate: deleteMutate } = useMutation(deleteProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);

      navigate("/management");
      toast.success("삭제되었습니다!");
    },
    onError: () => {
      toast.error("삭제 실패하였습니다. 잠시 후 다시 시도해주세요.");
    },
  });

  const { mutate: completeMutate } = useMutation(completeProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
      queryClient.invalidateQueries(["completeProjectData"]);

      navigate("/management");
      toast.success("완료 처리되었습니다!");
    },
    onError: () => {
      toast.error("완료 처리 실패하였습니다. 잠시 후 다시 시도해주세요.");
    },
  });

  const { mutate: editMutate } = useMutation(editProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);

      navigate("/management");
      toast.success("수정되었습니다!");
    },
    onError: () => {
      toast.error("수정 실패하였습니다. 잠시 후 다시 시도해주세요.");
    },
  });

  const { mutate: leaveMutate } = useMutation(leaveProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
      toast.success("팀에서 탈퇴되었습니다!");
    },
    onError: () => {
      toast.error("팀 탈퇴를 실패하였습니다. 잠시 후 다시 시도해주세요.");
    },
  });

  return {
    createMutate,
    deleteMutate,
    completeMutate,
    editMutate,
    leaveMutate,
  };
};

export default useManagementMutation;
