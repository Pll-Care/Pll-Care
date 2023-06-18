import { useQuery } from "react-query";
import { useDispatch, useSelector } from "react-redux";
import { getMeetingRecord } from "../lib/apis/meetingRecordManagementApi";
import useMeetingRecordManagementMutation from "./hooks/useMeetingRecordManagementMutation";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";
import { useEffect } from "react";

const SelectedMeetingRecord = ({ setContent, setTitle }) => {
  const selectedMeetingRecordId = useSelector(
    (state) => state.meetingRecordManagement.selectedMeetingRecordId
  );

  const dispatch = useDispatch();

  const isEdit = useSelector((state) => state.meetingRecordManagement.isEdit);

  const { deleteMutate, createBookMarkMutate } =
    useMeetingRecordManagementMutation();

  const fallback = {};
  const { data = fallback } = useQuery(
    ["managementSelectedMeetingRecordList", selectedMeetingRecordId],
    () => getMeetingRecord(selectedMeetingRecordId)
  );

  const handleEditMeetingRecord = () => {
    dispatch(meetingRecordManagementActions.onChangeIsEditState(true));
    dispatch(
      meetingRecordManagementActions.onEditSelectedMeetingRecordState(false)
    );
  };

  const handleDeleteMeetingRecord = () => {
    deleteMutate(data.memoId);
    dispatch(meetingRecordManagementActions.onEditInitialState(true));
  };

  const handleBookMarkMeetingRecord = () => {
    createBookMarkMutate(data.memoId);
    dispatch(meetingRecordManagementActions.onEditInitialState(true));
  };

  useEffect(() => {
    dispatch(meetingRecordManagementActions.setTitle(data.title));
    dispatch(meetingRecordManagementActions.setContent(data.content));
  })

  return (
    <div className="selected-meeting-record">
      <div className="selected-meeting-record-heading">
        <div className="meeting-record-date">
          {new Date(data.createdDate).toLocaleString()}
        </div>
        <div className="meeting-record-title">{data.title}</div>
        <div className="meeting-record-container">
          <div className="meeting-record-author">{data.author}</div>
          <div className="meeting-record-button-wrapper">
            <button onClick={handleEditMeetingRecord}>수정하기</button>
            <button onClick={handleDeleteMeetingRecord}>삭제하기</button>
            <button onClick={handleBookMarkMeetingRecord}>북마크하기</button>
          </div>
        </div>
      </div>
      <div
        className="selected-meeting-record-content"
        dangerouslySetInnerHTML={{ __html: data.content }}
      />
    </div>
  );
};

export default SelectedMeetingRecord;
