import { useQuery } from "react-query";
import ApplicationItem from "./ApplicationItem";
import { getApplicationUser } from "../../lib/apis/teamMemberManagementApi";

const ApplicationStatus = ({ projectId }) => {
  const { data: users = [] } = useQuery(["applyUsers", projectId], () =>
    getApplicationUser(projectId)
  );

  return (
    <section className="applicationStatus">
      <div>
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
                key={user.id}
                userId={user.id}
                name={user.name}
                job={user.position}
                imageUrl={user.imageUrl}
              />
            ))
          )}
        </ul>
      </div>
    </section>
  );
};

export default ApplicationStatus;
