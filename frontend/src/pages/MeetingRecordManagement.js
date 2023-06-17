import MeetingRecordEditor from "../components/MeetingRecordManagement/MeetingRecordEditor";
import MeetingRecordList from "../components/MeetingRecordManagement/MeetingRecordList";

const MeetingRecordManagement = () => {
  return (
    <div className="meeting-record-management-wrapper">
      <div className="meeting-record-management">
        <MeetingRecordList />
        <MeetingRecordEditor />
      </div>
    </div>
  );
};

export default MeetingRecordManagement;
