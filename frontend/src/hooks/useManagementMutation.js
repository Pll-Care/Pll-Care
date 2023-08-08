import { useQueryClient, useMutation } from "react-query";

import { toast } from "react-toastify";

import { useRouter } from "./useRouter";

import {
  completeProject,
  createProject,
  deleteProject,
  editProject,
  leaveProject,
} from "../lib/apis/managementApi";

const useManagementMutation = () => {
  const queryClient = useQueryClient();
  const { routeTo, routeOptionTo } = useRouter();

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

      // TODO: 삭제한 프로젝트 접근 했을 때 '존재하지 않는 프로젝트입니다' 처리하기
      routeOptionTo("/management", { replace: true });
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

      routeTo("/management");
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

      routeTo("/management");
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
