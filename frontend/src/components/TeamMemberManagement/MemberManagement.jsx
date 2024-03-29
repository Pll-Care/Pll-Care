import { useCallback, useState } from "react";
import Button from "../common/Button";
import MemberItem from "./MemberItem";
import { useQuery } from "react-query";
import { useProjectDetail } from "../../context/ProjectDetailContext";
import { useManagementClient } from "../../context/Client/ManagementClientContext";

const MemberManagement = () => {
  const [isEdit, setIsEdit] = useState(false);
  const { isLeader, projectId } = useProjectDetail();
  const { getTeamMember } = useManagementClient();

  const {
    isLoading,
    data: { data } = [],
    refetch,
  } = useQuery(["members", projectId], () => getTeamMember(projectId));

  const editMember = useCallback(() => {
    setIsEdit(true);
  }, []);

  const editCompleted = useCallback(() => {
    setIsEdit(false);
  }, []);
  return (
    <section className="memberMangement">
      <div className="memberMangement_head">
        <span className="teamMember_title">팀원 관리</span>
        {isLeader ? (
          isEdit ? (
            <Button text="수정완료" size="small" onClick={editCompleted} />
          ) : (
            <Button text="수정" size="small" onClick={editMember} />
          )
        ) : null}
      </div>
      <div>
        <ul className="memberMangement_members">
          {isLoading ? (
            <div>로딩중....</div>
          ) : data?.length === 0 ? (
            <p className="memberMangement_members_noMember">팀원이 없습니다.</p>
          ) : (
            data?.map((member) => (
              <MemberItem
                key={member.memberId}
                memberId={member.memberId}
                name={member.name}
                position={member.position}
                imageUrl={member.imageUrl}
                isEdit={isEdit}
                refetch={refetch}
              />
            ))
          )}
        </ul>
      </div>
    </section>
  );
};
export default MemberManagement;
