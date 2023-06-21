import { useQuery } from "react-query";
import { useDispatch, useSelector } from "react-redux";
import { getMeetingRecord } from "../lib/apis/meetingRecordManagementApi";
import useMeetingRecordManagementMutation from "./hooks/useMeetingRecordManagementMutation";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";
import { useEffect, useState } from "react";

const MeetingRecord = ({ state }) => {
  const selectedMeetingRecordId = useSelector(
    (state) => state.meetingRecordManagement.selectedMeetingRecordId
  );

  const createdMeetingRecordId = useSelector(
    (state) => state.meetingRecordManagement.createdMeetingRecordId
  );

  const [data, setData] = useState({});

  const dispatch = useDispatch();

  const { deleteMutate, createBookMarkMutate } =
    useMeetingRecordManagementMutation();

  const fallback = {};
  const { data: createdData = fallback } = useQuery(
    ["managementCreatedMeetingRecordList", createdMeetingRecordId],
    () => getMeetingRecord(createdMeetingRecordId),
    {
      enabled: state === "createdMeetingRecord",
    }
  );

  const { data: selectedData = fallback } = useQuery(
    ["managementSelectedMeetingRecordList", selectedMeetingRecordId],
    () => getMeetingRecord(selectedMeetingRecordId),
    {
      enabled: state === "selectedMeetingRecord",
    }
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
  }, []);

  return state === "selectedMeetingRecord" ? (
    <div className="meeting-record">
      <div className="meeting-record-heading">
        <div className="meeting-record-date">
          {new Date(selectedData.createdDate).toLocaleString()}
        </div>
        <div className="meeting-record-title">{selectedData.title}</div>
        <div className="meeting-record-container">
          <div className="meeting-record-author">{selectedData.author}</div>
          <div className="meeting-record-button-wrapper">
            <button onClick={handleEditMeetingRecord}>수정하기</button>
            <button onClick={handleDeleteMeetingRecord}>삭제하기</button>
            <button onClick={handleBookMarkMeetingRecord}>북마크하기</button>
          </div>
        </div>
      </div>
      <div
        className="meeting-record-content"
        dangerouslySetInnerHTML={{ __html: selectedData.content }}
      />
    </div>
  ) : (
    <div className="meeting-record">
      <div className="meeting-record-heading">
        <div className="meeting-record-date">
          {new Date(createdData.createdDate).toLocaleString()}
        </div>
        <div className="meeting-record-title">{createdData.title}</div>
        <div className="meeting-record-container">
          <div className="meeting-record-author">{createdData.author}</div>
          <div className="meeting-record-button-wrapper">
            <button onClick={handleEditMeetingRecord}>수정하기</button>
            <button onClick={handleDeleteMeetingRecord}>삭제하기</button>
            <button onClick={handleBookMarkMeetingRecord}>북마크하기</button>
          </div>
        </div>
      </div>
      <div
        className="selected-meeting-record-content"
        dangerouslySetInnerHTML={{ __html: createdData.content }}
      />
    </div>
  );
};

export default MeetingRecord;
