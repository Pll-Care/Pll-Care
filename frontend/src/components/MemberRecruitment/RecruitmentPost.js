import { Link, useParams } from "react-router-dom";

import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";

import react from "./img/react.png";
import firebase from "./img/firebase.png";
import vue from "./img/vue.png";

const RecruitmentPost = () => {
  const { id } = useParams();
  const like = false;
  return (
    <Link to={`/recruitment/${id}`} className="recruitment-post-link">
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
            <h2>ABC 프로젝트</h2>
            {like ? (
              <FavoriteIcon className="post-icon" />
            ) : (
              <FavoriteBorderIcon className="post-icon" />
            )}
          </div>
          <h3>모집 기간: ----</h3>
          <h3>모집 포지션: -----</h3>
        </div>
      </div>
    </Link>
  );
};
export default RecruitmentPost;
