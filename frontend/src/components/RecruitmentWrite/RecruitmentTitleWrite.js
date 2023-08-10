import { Link } from "react-router-dom";

import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";

import projectDefaultImg from "../../assets/project-default-img.jpg";

const RecruitmentTitleWrite = ({
  imageUrl,
  handleChange,
  formValues,
  inputRefs,
}) => {
  return (
    <div className="member-title">
      <Link to="/recruitment">
        <ArrowBackIosNewIcon className="recruitment-direction" />
      </Link>
      {imageUrl ? (
        <img src={imageUrl} alt="" />
      ) : (
        <img src={projectDefaultImg} alt="" />
      )}

      <input
        type="text"
        name="title"
        ref={inputRefs.title}
        value={formValues.title}
        onChange={handleChange}
        placeholder="모집글 제목을 작성해보세요"
      />
    </div>
  );
};
export default RecruitmentTitleWrite;
