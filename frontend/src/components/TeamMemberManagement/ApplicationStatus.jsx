import { useQuery } from "react-query";
import ApplicationItem from "./ApplicationItem";
import { useProjectDetail } from "../../context/ProjectDetailContext";
import { useManagementClient } from "../../context/Client/ManagementClientContext";

const ApplicationStatus = () => {
  const { projectId } = useProjectDetail();
  const { getApplicationUser } = useManagementClient();

  const { data: { data } = [], refetch } = useQuery(
    ["applyUsers", projectId],
    () => getApplicationUser(projectId)
  );

  return (
    <section className="applicationStatus">
      <div className="memberMangement_head">
        <span className="teamMember_title">지원 현황</span>
      </div>
      <div>
        {data && (
          <ul className="applicationStatus_users">
            {data?.length === 0 ? (
              <p className="applicationStatus_users_noMember">
                지원 인원이 없습니다.
              </p>
            ) : (
              data?.map((user) => (
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
        )}
      </div>
    </section>
  );
};

export default ApplicationStatus;
