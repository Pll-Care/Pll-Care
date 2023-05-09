import { Link } from "react-router-dom";

const ManagementHeader = (projectId) => {
  return (
    <header className="management-header">
      <div className="management-header-overview">
        <Link
          to={`/management/${projectId}/overview`}
          style={{ textDecoration: "none" }}
        >
          오버뷰
        </Link>
      </div>

      <div className="management-header-meeting-record">
        <Link
          to={`/management/${projectId}/meetingRecord`}
          style={{ textDecoration: "none" }}
        >
          회의록
        </Link>
      </div>

      <div className="management-header-schedule">
        <Link
          to={`/management/${projectId}/schedule`}
          style={{ textDecoration: "none" }}
        >
          일정
        </Link>
      </div>

      <div className="management-header-evaluation">
        <Link
          to={`/management/${projectId}/evaluation`}
          style={{ textDecoration: "none" }}
        >
          평가
        </Link>
      </div>
    </header>
  );
};

export default ManagementHeader;
