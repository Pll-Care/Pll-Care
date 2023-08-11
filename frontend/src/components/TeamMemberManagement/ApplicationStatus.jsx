import { useQuery } from "react-query";
import ApplicationItem from "./ApplicationItem";
import { getApplicationUser } from "../../lib/apis/teamMemberManagementApi";
import { useProjectDetail } from "../../context/ProjectDetailContext";

const ApplicationStatus = () => {
  const { projectId } = useProjectDetail();

  const { data: users = [], refetch } = useQuery(
    ["applyUsers", projectId],
    () => getApplicationUser(projectId)
  );

  return (
    <section className="applicationStatus">
      <div className="memberMangement_head">
        <span className="teamMember_title">지원 현황</span>
      </div>
      <div>
        <ul className="applicationStatus_users">
          {users.length === 0 ? (
            <p className="applicationStatus_users_noMember">
              지원 인원이 없습니다.
            </p>
          ) : (
            users.map((user) => (
              <ApplicationItem
                key={user.memberId}
                memberId={user.memberId}
                name={user.name}
                position={user.position}
                imageUrl={user.imageUrl}
                postId={user.postId}
                projectId={projectId}
                refetch={refetch}
              />
            ))
          )}
        </ul>
      </div>
    </section>
  );
};

export default ApplicationStatus;
