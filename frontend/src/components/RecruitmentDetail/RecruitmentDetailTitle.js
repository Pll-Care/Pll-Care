import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import { Link } from "react-router-dom";

const RecruitmentDetailTitle = () => {
  return (
    <div className="detail_title">
      <div className="detail_title_content">
        <Link to="/recruitment" className="mui_arrow">
          <ArrowBackIosNewIcon />
        </Link>
        <img src="https://cdn.pixabay.com/photo/2015/01/08/18/25/desk-593327_640.jpg" />
        <h1>Project Title</h1>
      </div>
      <div className="detail_title_plus">
        <h2>2023.4.20 작성</h2>
        <h5>조회수100</h5>
      </div>
    </div>
  );
};
export default RecruitmentDetailTitle;
