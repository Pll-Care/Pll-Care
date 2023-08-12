import { Link } from "react-router-dom";
import PROJECT_DEFAULT_IMG from "../../../assets/project-default-img.jpg";

const ProjectItem = ({ postId, title, description }) => {
  return (
    <li>
      <Link className="myProject_item" to={`/recruitment/${postId}`}>
        <div className="myProject_item_img">
          <img src={PROJECT_DEFAULT_IMG} alt="프로젝트 이미지" />
        </div>
        <div className="myProject_item_wrap">
          <div className="myProject_item_wrap_title">{title}</div>
          <div className="myProject_item_wrap_description">
            <div>{description}</div>
          </div>
        </div>
      </Link>
    </li>
  );
};

export default ProjectItem;
