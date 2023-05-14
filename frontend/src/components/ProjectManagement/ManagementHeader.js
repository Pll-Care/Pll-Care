import { Link } from "react-router-dom";

const ManagementHeader = ({id}) => {
  return (
    <header className="management-detail-header">
      <div className="management-detail-header-overview">
        <Link
          to={`/management/${id}/overview`}
          style={{ textDecoration: "none" }}
        >
          오버뷰
        </Link>
      </div>

      <div className="management-detail-header-meeting-record">
        <Link
          to={`/management/${id}/meetingRecord`}
          style={{ textDecoration: "none" }}
        >
          회의록
        </Link>
      </div>

      <div className="management-detail-header-schedule">
        <Link
          to={`/management/${id}/schedule`}
          style={{ textDecoration: "none" }}
        >
          일정
        </Link>
      </div>

      <div className="management-detail-header-evaluation">
        <Link
          to={`/management/${id}/evaluation`}
          style={{ textDecoration: "none" }}
        >
          평가
        </Link>
      </div>
    </header>
  );
};

export default ManagementHeader;
