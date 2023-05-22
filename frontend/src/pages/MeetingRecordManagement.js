import MeetingRecordList from "../components/MeetingRecordManagement/MeetingRecordList";
import MeetingRecordListEditor from "../components/MeetingRecordManagement/MeetingRecordListEditor";

const MeetingRecordManagement = () => {
  return (
    <div className='meeting-record-management-wrapper'>
      <div className='meeting-record-management'>
        <MeetingRecordList />
        <MeetingRecordListEditor />
      </div>
    </div>
  )
};

export default MeetingRecordManagement;
