import { Link } from "react-router-dom";

import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";

import react from "./img/react.png";
import firebase from "./img/firebase.png";
import vue from "./img/vue.png";

const RecruitmentPost = ({ data }) => {
  console.log(data);
  const filteredPositions = data?.recruitInfoList
    .filter((recruitment) => recruitment.totalCnt - recruitment.currentCnt > 0)
    .map((recruitment) => recruitment.position);

  return (
    <Link to={`/recruitment/${data?.postId}`} className="recruitment-post-link">
      <div className="recruitment-post">
        <img
          src="https://cdn.pixabay.com/photo/2015/01/08/18/25/desk-593327_640.jpg"
          alt=""
        />
        <div className="recruitment-post-stack">
          <img src={react} alt="" />
          <img src={firebase} alt="" />
          <img src={vue} alt="" />
        </div>

        <div className="recruitment-post-project">
          <div className="recruitment-post-project-title">
            <h2>{data?.title}</h2>
            {data?.liked ? (
              <FavoriteIcon className="post-icon" />
            ) : (
              <FavoriteBorderIcon className="post-icon" />
            )}
          </div>
          <h3>모집 기간</h3>
          <h6>
            {data?.recruitStartDate} ~ {data?.recruitEndDate}
          </h6>
          <h3>모집 포지션</h3>
          {filteredPositions?.map((item, index) => (
            <h6 key={index}>{item}</h6>
          ))}
        </div>
      </div>
    </Link>
  );
};
export default RecruitmentPost;