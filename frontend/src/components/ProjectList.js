import ProjectItem from "./ProjectItem";

const ProjectList = ({ projectList }) => {
  return (
    <div className="project-list">
      <div>
        {projectList.map((it) => (
          <ProjectItem key={it.id} {...it} />
        ))}
      </div>
    </div>
  );
};

ProjectList.defaultProps = {
  projectList: [],
};

export default ProjectList;
