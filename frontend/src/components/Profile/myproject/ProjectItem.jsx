import PROJECT_DEFAULT_IMG from "../../../assets/project-default-img.jpg";

const ProjectItem = ({ projectId, title, description }) => {
  return (
    <li className="myProject_item">
      <div className="myProject_item_img">
        <img src={PROJECT_DEFAULT_IMG} alt="프로젝트 이미지" />
      </div>
      <div className="myProject_item_wrap">
        <div className="myProject_item_wrap_title">{title}</div>
        <div className="myProject_item_wrap_description">
          <div>{description}</div>
        </div>
      </div>
    </li>
  );
};

export default ProjectItem;
