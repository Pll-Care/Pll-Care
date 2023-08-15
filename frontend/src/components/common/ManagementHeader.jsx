import { Link } from "react-router-dom";
import { useProjectDetail } from "../../context/ProjectDetailContext";

const ManagementHeader = () => {
  const { isLeader, projectId } = useProjectDetail();

  return (
    <header className="management-header">
      <Link to={`/management/${projectId}/overview`}>오버뷰</Link>
      <Link to={`/management/${projectId}/meetingRecord`}>회의록</Link>
      <Link to={`/management/${projectId}/schedule`}>일정</Link>
      <Link to={`/management/${projectId}/evaluation`}>평가</Link>
      <Link to={`/management/${projectId}/teamMember`}>팀 관리</Link>
      {isLeader && <Link to={`/management/${projectId}/project`}>관리</Link>}
    </header>
  );
};

export default ManagementHeader;
