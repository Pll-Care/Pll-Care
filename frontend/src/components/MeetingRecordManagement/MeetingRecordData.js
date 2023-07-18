import { useDispatch } from "react-redux";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";

const MeetingRecordData = ({ sortedMeetingRecordList }) => {
  const dispatch = useDispatch();

  const handleClickMeetingRecord = (e) => {
    dispatch(
      meetingRecordManagementActions.setSelectedMeetingRecordId(e.target.id)
    );
    dispatch(meetingRecordManagementActions.setInitialState(false));
    dispatch(
      meetingRecordManagementActions.setIsCreatedMeetingRecordVisibleState(
        false
      )
    );
    dispatch(
      meetingRecordManagementActions.setSelectedMeetingRecordState(true)
    );
  };

  return (
    <div className="meeting-record-all-meeting-record-list-data">
      {Array.isArray(sortedMeetingRecordList) &&
        sortedMeetingRecordList.map((record) => (
          <div
            className="meeting-record-item"
            key={record.memoId}
            onClick={handleClickMeetingRecord}
            id={record.memoId}
          >
            <div className="meeting-record-item-date">
              {new Date(record.createdDate).toLocaleDateString()}
            </div>
            <div className="meeting-record-item-title">{record.title}</div>
            <div className="meeting-record-item-writer">{record.author}</div>
          </div>
        ))}
    </div>
  );
};

export default MeetingRecordData;
