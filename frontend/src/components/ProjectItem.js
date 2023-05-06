const ProjectItem = ({ id, title, content, date }) => {
  return (
    <div className="project-item">
      <div className="project-item-title">프로젝트명 : {title}</div>
      <div className="project-item-content">내용 : {content}</div>
      <div className="project-item-date">
        생성일 : {new Date(date).toLocaleString()}{" "}
      </div>
    </div>
  );
};

export default ProjectItem;
