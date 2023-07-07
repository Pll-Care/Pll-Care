import react from "./img/react.png";
import firebase from "./img/firebase.png";
import vue from "./img/vue.png";
import { Link, useParams } from "react-router-dom";
import FavoriteIcon from "@mui/icons-material/Favorite";
import { useState } from "react";

const RecruitmentPost = () => {
  const { id } = useParams();
  const [like, setLike] = useState(false);
  return (
    <Link to={`/recruitment/${id}`}>
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
            <FavoriteIcon
              className={`post-icon ${like ? "liked" : ""}`}
              onClick={(event) => {
                event.stopPropagation();
                setLike(!like);
              }}
            />
          </div>
          <h3>모집 기간: ----</h3>
          <h3>모집 포지션: -----</h3>
        </div>
      </div>
    </Link>
  );
};
export default RecruitmentPost;
