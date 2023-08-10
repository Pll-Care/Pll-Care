import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import Quill from "quill";
import ImageResize from "quill-image-resize";
import { useRef } from "react";

Quill.register("modules/ImageResize", ImageResize);

const RecruitmentDetailDescription = ({
  data,
  isEdit,
  formValues,
  setFormValues,
  handleChange,
  inputRefs,
}) => {
  const quillRef = useRef(null);

  // 모집글 설명 작성
  const handleChangeDescription = (content) => {
    setFormValues((prevState) => ({
      ...prevState,
      description: content,
    }));
  };
  return (
    <>
      <div className="recruitment-detail-description">
        <h4>설명</h4>
        {isEdit ? (
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
        ) : (
          <div dangerouslySetInnerHTML={{ __html: data?.description }} />
        )}
      </div>

      <div className="recruitment-detail-description">
        <h4>레퍼런스</h4>
        {isEdit ? (
          <textarea
            onChange={handleChange}
            value={formValues.reference}
            name="reference"
            ref={inputRefs.reference}
          />
        ) : (
          <h5>{data?.reference}</h5>
        )}
      </div>

      <div className="recruitment-detail-description">
        <h4>컨택</h4>
        {isEdit ? (
          <textarea
            onChange={handleChange}
            value={formValues.contact}
            name="contact"
            ref={inputRefs.contact}
          />
        ) : (
          <h5>{data?.contact}</h5>
        )}
      </div>
    </>
  );
};
export default RecruitmentDetailDescription;
