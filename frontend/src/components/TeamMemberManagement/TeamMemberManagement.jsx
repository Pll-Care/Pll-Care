import ApplicationStatus from "./ApplicationStatus";
import MemberManagement from "./MemberManagement";

const TeamMemberManagement = () => {
  return (
    <div className="teamMember">
      <MemberManagement />
      <ApplicationStatus />
    </div>
  );
};

export default TeamMemberManagement;
