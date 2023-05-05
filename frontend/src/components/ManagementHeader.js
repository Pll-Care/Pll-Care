import { Link } from "react-router-dom";

const ManagementHeader = () => {
  return (
    <header className="management-header">
      <div className="management-header-overview">
        <Link to="/management/overview" style={{ textDecoration: "none" }}>
          오버뷰
        </Link>
      </div>

      <div className="management-header-meeting-record">
        <Link to="/management/meetingRecord" style={{ textDecoration: "none" }}>
          회의록
        </Link>
      </div>

      <div className="management-header-schedule">
        <Link to="/management/schedule" style={{ textDecoration: "none" }}>
          일정
        </Link>
      </div>

      <div className="management-header-evaluation">
        <Link to="/management/evaluation" style={{ textDecoration: "none" }}>
          평가
        </Link>
      </div>
    </header>
  );
};

export default ManagementHeader;
