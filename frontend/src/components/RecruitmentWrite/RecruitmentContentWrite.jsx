import { Fragment } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import Quill from "quill";
import ImageResize from "quill-image-resize";
import { useRef } from "react";
import { useEditorImageUploader } from "../../hooks/useEditorImageHandler";

Quill.register("modules/ImageResize", ImageResize);

const RecruitmentContentWrite = ({
  formValues,
  setFormValues,
  handleChange,
  inputRefs,
}) => {
  const quillRef = useRef(null);
  useEditorImageUploader(quillRef.current);

  // 모집글 설명 작성
  const handleChangeDescription = (content) => {
    setFormValues((prevState) => ({
      ...prevState,
      description: content,
    }));
  };
  return (
    <Fragment>
      <div className="member-content-description">
        <h3>설명</h3>
        <ReactQuill
          className="react-quill"
          ref={quillRef}
          name="description"
          value={formValues.description}
          onChange={handleChangeDescription}
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

        <h3>레퍼런스</h3>
        <textarea
          onChange={handleChange}
          value={formValues.reference}
          name="reference"
          ref={inputRefs.reference}
        />
      </div>

      <div className="member-content-contact">
        <h3>컨택</h3>
        <textarea
          onChange={handleChange}
          value={formValues.contact}
          name="contact"
          ref={inputRefs.contact}
        />
      </div>
    </Fragment>
  );
};
export default RecruitmentContentWrite;
