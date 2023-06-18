import { useDispatch, useSelector } from "react-redux";

import Button from "../shared/Button";

import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import Quill from "quill";
import ImageResize from "quill-image-resize";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";
import { useEffect, useState } from "react";
import useMeetingRecordManagementMutation from "./hooks/useMeetingRecordManagementMutation";
import { useLocation } from "react-router-dom";
import SelectedMeetingRecord from "./SelectedMeetingRecord";

Quill.register("modules/ImageResize", ImageResize);

const MeetingRecordEditor = () => {
  const [content, setContent] = useState("");
  const [title, setTitle] = useState("");
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

  const { createMutate } = useMeetingRecordManagementMutation();

  const dispatch = useDispatch();

  const handleInitialState = () => {
    dispatch(meetingRecordManagementActions.onEditInitialState(false));
    dispatch(
      meetingRecordManagementActions.onEditSelectedMeetingRecordState(false)
    );
  };

  const handleChangeTitle = (e) => {
    setTitle(e.target.value);
  };

  const handleChangeContent = (e) => setContent(e);

  const handleSubmit = () => {
    createMutate({
      projectId: projectId,
      title: title,
      content: content,
    });
    dispatch(
      meetingRecordManagementActions.onEditSelectedMeetingRecordState(false)
    );
    dispatch(
      meetingRecordManagementActions.onEditIsCreatedMeetingRecordVisibleState(
        true
      )
    );
  };

  useEffect(() => {
    dispatch(meetingRecordManagementActions.onEditInitialState(true));
    dispatch(
      meetingRecordManagementActions.onEditSelectedMeetingRecordState(false)
    );
    dispatch(
      meetingRecordManagementActions.onEditIsCreatedMeetingRecordVisibleState(
        false
      )
    );
  }, []);

  return (
    <div className="meeting-record-new-meeting-record-editor">
      {initialState ? (
        <div className="meeting-record-initial-state">
          <h1 className="meeting-record-heading">회의록을 작성해보세요!</h1>
          <Button text={"작성하기"} onClick={handleInitialState} />
        </div>
      ) : isSelectedMeetingRecord ? (
        <SelectedMeetingRecord />
      ) : isCreatedMeetingRecordVisible ? (
        <div>
          <div>createdMeetingRecord</div>
        </div>
      ) : (
        <div className="meeting-record-editor">
          <div className="meeting-record-title">
            <h1>제목: </h1>
            <input value={title} onChange={handleChangeTitle} />
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
          <Button text={"작성 완료하기"} onClick={handleSubmit} />
        </div>
      )}
    </div>
  );
};

export default MeetingRecordEditor;
