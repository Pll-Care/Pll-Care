import { useQueryClient, useMutation } from "react-query";

import { useRouter } from "../useRouter";
import { useManagementClient } from "../../context/Client/ManagementClientContext";

const useManagementMutation = () => {
  const queryClient = useQueryClient();
  const { routeTo, routeOptionTo } = useRouter();
  const {
    createProject,
    deleteProject,
    completeProject,
    editProject,
    leaveProject,
  } = useManagementClient();

  const { mutate: createMutate } = useMutation(createProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
    },
  });

  const { mutate: deleteMutate } = useMutation(deleteProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);

      // TODO: 삭제한 프로젝트 접근 했을 때 '존재하지 않는 프로젝트입니다' 처리하기
      routeOptionTo("/management", { replace: true });
    },
  });

  const { mutate: completeMutate } = useMutation(completeProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
      queryClient.invalidateQueries(["completeProjectData"]);

      routeTo("/management");
    },
  });

  const { mutate: editMutate } = useMutation(editProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);

      routeTo("/management");
    },
  });

  const { mutate: leaveMutate } = useMutation(leaveProject, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementOngoingProjectList"]);
      queryClient.invalidateQueries(["managementAllProjectList"]);
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
