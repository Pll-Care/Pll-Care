import { Link } from "react-router-dom";

const ManagementHeader = ({ id }) => {
  return (
    <header className="management-header">
      <Link to={`/management/${id}/overview`}>오버뷰</Link>
      <Link to={`/management/${id}/meetingRecord`}>회의록</Link>
      <Link to={`/management/${id}/schedule`}>일정</Link>
      <Link to={`/management/${id}/evaluation`}>평가</Link>
      <Link to={`/management/${id}/teamMember`}>팀 관리</Link>
      <Link to={`/management/${id}/project`}>관리</Link>
    </header>
  );
};

export default ManagementHeader;
