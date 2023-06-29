import { useParams } from "react-router-dom";
import ApplicationStatus from "./ApplicationStatus";
import MemberManagement from "./MemberManagement";

const TeamMemberManagement = () => {
  const { id: projectId } = useParams();

  return (
    <div className="teamMember">
      <MemberManagement projectId={projectId} />
      <ApplicationStatus projectId={projectId} />
    </div>
  );
};

export default TeamMemberManagement;
