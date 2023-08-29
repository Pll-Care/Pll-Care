import Button from "../common/Button";

import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import Quill from "quill";
import ImageResize from "quill-image-resize";
import { useRef } from "react";

import { useEditorImageUploader } from "../../hooks/useEditorImageHandler";

Quill.register("modules/ImageResize", ImageResize);

const NewMeetingRecord = ({
  title,
  content,
  handleChangeTitle,
  handleSubmit,
  handleChangeContent,
}) => {
  const quillRef = useRef(null);
  useEditorImageUploader(quillRef.current);

  return (
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
          text={"작성 완료"}
          onClick={handleSubmit}
        />
      </div>
      <ReactQuill
        className="react-quill"
        ref={quillRef}
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
            modules: ["Resize", "DisplaySize", "Toolbar"],
          },
        }}
      />
    </div>
  );
};

export default NewMeetingRecord;
