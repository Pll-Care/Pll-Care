import { Routes, Route, Link } from "react-router-dom";

import Overview from "../pages/OverviewManagement";
import MeetingRecord from "../pages/MeetingRecordManagement";
import Schedule from "../pages/ScheduleManagement";
import Evaluation from "../pages/EvaluationManagement";

const ManagementHeader = () => {
  return (
    <header className="management-header">
      <div className="management-header-overview">
        <Link to="/overview" style={{ textDecoration: "none" }}>
          오버뷰
        </Link>
      </div>

      <div className="management-header-meeting-record">
        <Link to="/meetingRecord" style={{ textDecoration: "none" }}>
          회의록
        </Link>
      </div>

      <div className="management-header-schedule">
        <Link to="/schedule" style={{ textDecoration: "none" }}>
          일정
        </Link>
      </div>

      <div className="management-header-evaluation">
        <Link to="/evaluation" style={{ textDecoration: "none" }}>
          평가
        </Link>
      </div>

      <Routes>
        <Route path="/overview" element={<Overview />}></Route>
        <Route path="/meetingRecord" element={<MeetingRecord />}></Route>
        <Route path="/schedule" element={<Schedule />}></Route>
        <Route path="/evaluation" element={<Evaluation />}></Route>
      </Routes>
    </header>
  );
};

export default ManagementHeader;
