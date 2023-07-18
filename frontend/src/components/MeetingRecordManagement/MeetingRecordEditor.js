import { useDispatch, useSelector } from "react-redux";
import { useLocation } from "react-router-dom";
import { useEffect } from "react";

import Button from "../common/Button";
import NewMeetingRecord from "./NewMeetingRecord";
import MeetingRecord from "./MeetingRecord";

import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";
import useMeetingRecordManagementMutation from "../../hooks/useMeetingRecordManagementMutation";

import { toast } from "react-toastify";
import { getProjectId } from "../../utils/getProjectId";
import { useQuery } from "react-query";
import { getCompleteProjectData } from "../../lib/apis/managementApi";

const MeetingRecordEditor = () => {
  const content = useSelector((state) => state.meetingRecordManagement.content);
  const title = useSelector((state) => state.meetingRecordManagement.title);

  const projectId = getProjectId(useLocation());

  const { data: isCompleted } = useQuery(['completeProjectData', projectId], () => getCompleteProjectData(projectId));

  const isCreatedMeetingRecordVisible = useSelector(
    (state) => state.meetingRecordManagement.isCreatedMeetingRecordVisible
  );
  const initialState = useSelector(
    (state) => state.meetingRecordManagement.initialState
  );
  const isSelectedMeetingRecord = useSelector(
    (state) => state.meetingRecordManagement.isSelectedMeetingRecord
  );
  const selectedMeetingRecordId = useSelector(
    (state) => state.meetingRecordManagement.selectedMeetingRecordId
  );

  const isEdit = useSelector((state) => state.meetingRecordManagement.isEdit);

  const { createMutate, editMutate } = useMeetingRecordManagementMutation();

  const dispatch = useDispatch();

  const handleInitialState = () => {
    dispatch(meetingRecordManagementActions.setInitialState(false));
    dispatch(
      meetingRecordManagementActions.setSelectedMeetingRecordState(false)
    );
  };

  const handleChangeTitle = (e) => {
    dispatch(meetingRecordManagementActions.setTitle(e.target.value));
  };

  const handleChangeContent = (e) =>
    dispatch(meetingRecordManagementActions.setContent(e));

  const handleSubmit = () => {
    if (title.length < 1) {
      toast.error("제목을 입력하세요.");
      return;
    } else if (content.length < 1) {
      toast.error("내용을 입력하세요.");
      return;
    }

    if (isEdit) {
      editMutate({
        selectedMeetingRecordId,
        title: title,
        content: content,
      });
    } else {
      createMutate({
        projectId: projectId,
        title: title,
        content: content,
      });
    }
  };

  useEffect(() => {
    dispatch(meetingRecordManagementActions.setInitialState(true));
  }, [dispatch, projectId]);

  return (
    <div className="meeting-record-new-meeting-record-editor">
      {initialState ? (
        <div className="meeting-record-initial-state">
          {isCompleted ? (
            <h1 className="meeting-record-heading">
              작성된 회의록을 확인해보세요!
            </h1>
          ) : (
            <h1 className="meeting-record-heading">회의록을 작성해보세요!</h1>
          )}
          {!isCompleted && (
            <Button text={"작성하기"} onClick={handleInitialState} />
          )}
        </div>
      ) : isSelectedMeetingRecord ? (
        <MeetingRecord state={"selectedMeetingRecord"} />
      ) : isCreatedMeetingRecordVisible ? (
        <MeetingRecord state={"createdMeetingRecord"} />
      ) : (
        <NewMeetingRecord
          title={title}
          content={content}
          handleChangeTitle={handleChangeTitle}
          handleSubmit={handleSubmit}
          handleChangeContent={handleChangeContent}
        />
      )}
    </div>
  );
};

export default MeetingRecordEditor;
