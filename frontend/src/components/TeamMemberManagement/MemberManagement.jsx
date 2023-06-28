import { useCallback, useState } from "react";
import Button from "../common/Button";
import MemberItem from "./MemberItem";
import { useQuery, useQueryClient } from "react-query";
import {
  deleteMember,
  getTeamMember,
} from "../../lib/apis/teamMemberManagementApi";
import useManagementTeamMemeber from "../../hooks/useManagementTeamMemeber";

const MemberManagement = ({ projectId }) => {
  const [isEdit, setIsEdit] = useState(false);
  const queryClient = useQueryClient();

  const { deleteTeamMemeber } = useManagementTeamMemeber();

  const { isLoading, data: response = [] } = useQuery(
    ["members", projectId],
    () => getTeamMember(projectId)
  );

  const editMember = useCallback(() => {
    setIsEdit(true);
  }, []);

  const editCompleted = useCallback(() => {
    setIsEdit(false);
  }, []);

  const deleteTeamMember = useCallback(
    async (memberId) => {
      const response = await deleteMember(projectId, memberId);
      if (response.status === 200) {
        queryClient.invalidateQueries(["members"]);
      }
      if (response.status === 401) {
        console.log(response.message);
      }
      // deleteTeamMemeber(projectId, memberId);
    },
    [projectId, queryClient]
  );

  return (
    <section className="memberMangement">
      <div className="memberMangement_head">
        <span className="teamMember_title">팀원 관리</span>
        {isEdit ? (
          <Button text="수정완료" size="small" onClick={editCompleted} />
        ) : (
          <Button text="수정" size="small" onClick={editMember} />
        )}
      </div>
      <div>
        <ul className="memberMangement_memebers">
          {isLoading ? (
            <div>로딩중....</div>
          ) : response.length === 0 ? (
            <p className="memberMangement_memebers_noMember">
              팀원이 없습니다.
            </p>
          ) : (
            response.map((member) => (
              <MemberItem
                key={member.id}
                memberId={member.id}
                name={member.name}
                position={member.position}
                imageUrl={member.imageUrl}
                isEdit={isEdit}
                deleteTeamMember={deleteTeamMember}
              />
            ))
          )}
        </ul>
      </div>
    </section>
  );
};
export default MemberManagement;
