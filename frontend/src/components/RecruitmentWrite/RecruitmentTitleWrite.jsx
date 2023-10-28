import { Link } from "react-router-dom";

import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import { useMediaQuery } from "@mui/material";

import { query } from "../../utils/mediaQuery";
import projectDefaultImg from "../../assets/project-default-img.jpg";

const RecruitmentTitleWrite = ({
  imageUrl,
  handleChange,
  formValues,
  inputRefs,
}) => {
  const isMobile = useMediaQuery(query);
  return (
    <div className="member-title">
      {isMobile && (
        <Link to="/recruitment">
          <ArrowBackIosNewIcon className="recruitment-direction" />
        </Link>
      )}

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
        placeholder="New Project Title"
      />
    </div>
  );
};
export default RecruitmentTitleWrite;
