/**
 * Project API 호출
 */

import ProjectItem from "./ProjectItem";

const ProjectList = () => {
  return (
    <ul className="project_list">
      {PROJECT_EXPERIENCES.length > 0 ? (
        PROJECT_EXPERIENCES.map((project) => (
          <ProjectItem
            key={project.projectId}
            title={project.title}
            description={project.description}
            startDate={project.startDate}
            endDate={project.endDate}
            techStack={project.techStack}
            projectId={project.projectId}
          />
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

const PROJECT_EXPERIENCES = [
  {
    title: "프로젝트 이름은 아직 미정입니다",
    description: "프로젝트 이름을 작성한 프로젝트 입니다.",
    startDate: "2023-07-10",
    endDate: "2023-11-11",
    techStack: ["HTML", "CSS", "React", "JavaScript", "TypeScirpt"],
    projectId: 5,
  },
  {
    title: "프로젝트 이름은 아직 미정입니다",
    description: "프로젝트 이름을 작성한 프로젝트 입니다.",
    startDate: "2023-07-10",
    endDate: "2023-11-11",
    techStack: ["HTML", "CSS", "React", "JavaScript", "TypeScirpt"],
    projectId: 4,
  },
  {
    title: "프로젝트 이름은 아직 미정입니다",
    description: "프로젝트 이름을 작성한 프로젝트 입니다.",
    startDate: "2023-07-10",
    endDate: "2023-11-11",
    techStack: ["HTML", "CSS", "React", "JavaScript", "TypeScirpt"],
    projectId: 3,
  },
  {
    title: "프로젝트 이름은 아직 미정입니다",
    description: "프로젝트 이름을 작성한 프로젝트 입니다.",
    startDate: "2023-07-10",
    endDate: "2023-11-11",
    techStack: ["HTML", "CSS", "React", "JavaScript", "TypeScirpt"],
    projectId: 2,
  },
  {
    title: "프로젝트 이름은 아직 미정입니다",
    description: "프로젝트 이름을 작성한 프로젝트 입니다.",
    startDate: "2023-07-10",
    endDate: "2023-11-11",
    techStack: ["HTML", "CSS", "React", "JavaScript", "TypeScirpt"],
    projectId: 1,
  },
];
