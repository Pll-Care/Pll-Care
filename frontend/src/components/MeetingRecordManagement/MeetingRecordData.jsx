import { useDispatch } from "react-redux";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";
import { useMediaQuery } from "@mui/material";

const MeetingRecordData = ({ sortedMeetingRecordList }) => {
  const dispatch = useDispatch();

  const isTablet = useMediaQuery("(min-width: 767px) and (max-width: 1024px)");

  const handleClickMeetingRecord = (e) => {
    if (e.target.id) {
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
    }
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
            <div className="meeting-record-item-date" id={record.memoId}>
              {new Date(record.createdDate).toLocaleDateString()}
            </div>
            <div className="meeting-record-item-title" id={record.memoId}>
              {isTablet
                ? record.title.length > 10
                  ? record.title.slice(0, 10) + "..."
                  : record.title
                : record.title.length > 16
                ? record.title.slice(0, 16) + "..."
                : record.title}
            </div>
            <div className="meeting-record-item-writer" id={record.memoId}>
              {record.author}
            </div>
          </div>
        ))}
    </div>
  );
};

export default MeetingRecordData;
