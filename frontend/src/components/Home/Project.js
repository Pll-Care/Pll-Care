import ProjectDetail from "./ProjectDetail";

const Project = ({ type }) => {
  return (
    <div className="project">
      <h1>{type}</h1>
      <ProjectDetail type={type} />
    </div>
  );
};

export default Project;
