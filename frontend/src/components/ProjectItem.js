import { Link } from "react-router-dom";

const ProjectItem = ({ projectId, title, content }) => {
  const getStringDate = (date) => {
    return date.toISOString().slice(0, 10);
  };

  return (
    <Link to={`/management/${projectId}/overview`}>
      <div className="project-item">
        <div className="project-item-thumbnail">
          <img alt="thumbnail" />
        </div>
        <div className="project-item-info">
          <div className="project-item-info-title">{title}</div>
          <div className="project-item-info-content">{content}</div>
          <div className="project-item-info-date">
            {getStringDate(new Date())}
          </div>
        </div>
      </div>
    </Link>
  );
};

export default ProjectItem;
