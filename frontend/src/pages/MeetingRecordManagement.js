import MeetingRecordEditor from "../components/MeetingRecordManagement/MeetingRecordEditor";
import MeetingRecordList from "../components/MeetingRecordManagement/MeetingRecordList";

const MeetingRecordManagement = () => {
  return (
    <div className="meeting-record-management">
      <MeetingRecordList />
      <MeetingRecordEditor />
    </div>
  );
};

export default MeetingRecordManagement;
