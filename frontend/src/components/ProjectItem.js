const ProjectItem = ({ id, title, content, date }) => {
  return (
    <div className="project-item">
      <div className="project-item-thumbnail">
        <img alt="thumbnail" />
      </div>
      <div className="project-item-info">
        <div className="project-item-info-title">{title}</div>
        <div className="project-item-info-content">{content}</div>
        <div className="project-item-info-date">
          {new Date(date).toLocaleString()}{" "}
        </div>
      </div>
    </div>
  );
};

export default ProjectItem;
