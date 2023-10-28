import { Link } from "react-router-dom";

import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";

import projectDefaultImg from "../../assets/project-default-img.jpg";

const RecruitmentPost = ({ data }) => {
  const filteredPositions = data?.recruitInfoList
    .filter((recruitment) => recruitment.totalCnt - recruitment.currentCnt > 0)
    .map((recruitment) => recruitment.position);

  return (
    <Link to={`/recruitment/${data?.postId}`} className="recruitment-post-link">
      <div className="recruitment-post">
        {data?.projectImageUrl ? (
          <img src={data?.projectImageUrl} alt="" />
        ) : (
          <img src={projectDefaultImg} alt="" />
        )}

        <div className="recruitment-post-stack">
          {data?.techStackList.slice(0, 7).map((tech) => (
            <img key={tech.name} src={tech.imageUrl} alt={tech.name} />
          ))}
        </div>

        <div className="recruitment-post-project">
          <div className="recruitment-post-project-title">
            <div className="recruitment-post-title">
              <h2>{data?.title}</h2>
            </div>

            <div className="recruitment-post-like">
              {data?.liked ? (
                <FavoriteIcon className="post-icon" fontSize="small" />
              ) : (
                <FavoriteBorderIcon className="post-icon" fontSize="small" />
              )}
              <h5>{data?.likeCount}</h5>
            </div>
          </div>

          <div className="recruitment-post-project-duration">
            <h5>모집 기간: </h5>
            <h6>
              {data?.recruitStartDate} ~ {data?.recruitEndDate}
            </h6>
          </div>

          <div className="recruitment-post-project-position">
            <h5>모집 포지션: </h5>
            <div className="recruitment-post-project-position-item">
              {filteredPositions?.map((item, index) => (
                <h6 key={index} className="item">
                  {item}
                </h6>
              ))}
            </div>
          </div>
        </div>
      </div>
    </Link>
  );
};
export default RecruitmentPost;
