import AllMeetingRecordList from "./AllMeetingRecordList";
import BookMarkedMeetingRecordList from "./BookMarkedMeetingRecordList";

const MeetingRecordList = () => {
  return (
    <div className="meeting-record-list">
      <BookMarkedMeetingRecordList />
      <AllMeetingRecordList />
    </div>
  );
};

export default MeetingRecordList;
