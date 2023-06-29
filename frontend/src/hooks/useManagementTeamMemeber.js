import { useMutation, useQueryClient } from "react-query";
import { deleteMember } from "../lib/apis/teamMemberManagementApi";

const useManagementTeamMemeber = () => {
  const queryClient = useQueryClient();

  const { mutate: deleteTeamMemeber } = useMutation(deleteMember, {
    onSuccess: (response) => {
      if (response.status === 401) return;
      /*
      FIXME: 서버에서 에러 response를 제공해서 성공했다고 처리됨. 그래서 succsee 의미가 없음
      */

      queryClient.invalidateQueries(["members"]);
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const { mutate: addTeamMemeber } = useMutation(deleteMember, {
    onSuccess: () => {
      queryClient.invalidateQueries(["members", "applyUsers"]);
    },
  });

  return { deleteTeamMemeber, addTeamMemeber };
};

export default useManagementTeamMemeber;
