import Button from "../common/Button";

import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import Quill from "quill";
import ImageResize from "quill-image-resize";
import { useEffect, useRef } from "react";

import { uploadImage } from "../../lib/apis/managementApi";

Quill.register("modules/ImageResize", ImageResize);

const NewMeetingRecord = ({
  title,
  content,
  handleChangeTitle,
  handleSubmit,
  handleChangeContent,
}) => {
  const quillRef = useRef(null);

  useEffect(() => {
    const handleImage = () => {
      const input = document.createElement("input");
      input.setAttribute("type", "file");
      input.setAttribute("accept", "image/*");
      input.click();

      input.onchange = async () => {
        const file = input.files[0];

        const range = quillRef.current.getEditor().getSelection(true);

        const reader = new FileReader();

        reader.readAsDataURL(file);
        reader.onloadend = async () => {
          const formData = new FormData();
          formData.append("file", file);

          const imgUrl = await uploadImage({
            dir: "memo",
            formData: formData.get("file"),
          });

          quillRef.current
            .getEditor()
            .insertEmbed(range.index, "image", imgUrl);

          quillRef.current.getEditor().setSelection(range.index + 1);
        };
      };
    };

    if (quillRef.current) {
      const toolbar = quillRef.current.getEditor().getModule("toolbar");
      toolbar.addHandler("image", handleImage);
    }
  }, []);

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
          text={"작성 완료하기"}
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
