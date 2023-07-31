import ProjectItem from "./ProjectItem";

const QUERY_KEY = "experience-project";
const ProjectList = ({ experienceData, refetch }) => {
  return (
    <ul className="project">
      {experienceData?.length > 0 ? (
        experienceData.map((projects) => (
          <li key={projects.year} className="project_devide-year">
            <div className="project_year">{projects.year}</div>
            <div className="project_list">
              {projects?.projectExperiences?.map((project, idx) => (
                <ProjectItem
                  key={QUERY_KEY + "-" + project.projectId}
                  title={project.title}
                  description={project.description}
                  startDate={project.startDate}
                  endDate={project.endDate}
                  techStack={project.techStack}
                  projectId={project.projectId}
                  refetch={refetch}
                />
              ))}
            </div>
          </li>
        ))
      ) : (
        <div className="project_list_blank">
          <span>진행한 프로젝트가 없습니다.</span>
        </div>
      )}
    </ul>
  );
};

export default ProjectList;
