import { useDispatch, useSelector } from "react-redux";

import Button from "../common/Button";

import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import Quill from "quill";
import ImageResize from "quill-image-resize";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";
import { useLayoutEffect } from "react";
import useMeetingRecordManagementMutation from "../../hooks/useMeetingRecordManagementMutation";
import { useLocation } from "react-router-dom";
import MeetingRecord from "./MeetingRecord";
import { toast } from "react-toastify";

Quill.register("modules/ImageResize", ImageResize);

const MeetingRecordEditor = () => {
  const content = useSelector((state) => state.meetingRecordManagement.content);
  const title = useSelector((state) => state.meetingRecordManagement.title);
  const projectId = parseInt(useLocation().pathname.slice(12, 14));
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
    dispatch(meetingRecordManagementActions.onEditInitialState(false));
    dispatch(
      meetingRecordManagementActions.onEditSelectedMeetingRecordState(false)
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

  useLayoutEffect(() => {
    if (!isEdit) {
      dispatch(meetingRecordManagementActions.onEditInitialState(true));
    } else {
      dispatch(meetingRecordManagementActions.onEditInitialState(false));
    }
    dispatch(
      meetingRecordManagementActions.onEditSelectedMeetingRecordState(false)
    );
    dispatch(
      meetingRecordManagementActions.onEditIsCreatedMeetingRecordVisibleState(
        false
      )
    );
  }, [dispatch]);

  return (
    <div className="meeting-record-new-meeting-record-editor">
      {initialState ? (
        <div className="meeting-record-initial-state">
          <h1 className="meeting-record-heading">회의록을 작성해보세요!</h1>
          <Button text={"작성하기"} onClick={handleInitialState} />
        </div>
      ) : isSelectedMeetingRecord ? (
        <MeetingRecord state={"selectedMeetingRecord"} />
      ) : isCreatedMeetingRecordVisible ? (
        <MeetingRecord state={"createdMeetingRecord"} />
      ) : (
        <div className="meeting-record-editor">
          <div className="meeting-record-title">
            <input
              value={title}
              onChange={handleChangeTitle}
              placeholder={"제목을 입력하세요"}
            />
            <Button
              size={"small"}
              type={"underlined"}
              text={"작성 완료하기"}
              onClick={handleSubmit}
            />
          </div>
          <ReactQuill
            className="react-quill"
            value={content}
            onChange={handleChangeContent}
            modules={{
              toolbar: [
                [{ header: [1, 2, 3, false] }],
                [{ size: ["small", false, "large", "huge"] }],
                ["bold", "italic", "underline", "strike"],
                [{ align: [] }],
                [{ color: [] }, { background: [] }],
                ["link", "image"],
              ],
              ImageResize: {
                parchment: Quill.import("parchment"),
              },
            }}
          />
        </div>
      )}
    </div>
  );
};

export default MeetingRecordEditor;
