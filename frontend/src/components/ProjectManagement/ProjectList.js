import { Link } from "react-router-dom";

import Button from "../../components/common/Button";

import useManagementMutation from "../../hooks/useManagementMutation";

const ProjectList = ({ projectList }) => {
  const { deleteMutate } = useManagementMutation();

  const handleCompleteProject = (e, projectId) => {
    e.preventDefault();
  };

  const handleRemoveProject = (e, projectId) => {
    e.preventDefault();

    deleteMutate(projectId);
  };

  return (
    <div className="project-list">
      {projectList?.map((project) => (
        <Link
          className="project-item"
          key={project.projectId}
          to={`/management/${project.projectId}/overview`}
        >
          <div className="project-item-left-col">
            <figure style={{ backgroundImage: `url(${project.imageUrl ? project.imageUrl : '/assets/project-default-img.jpg'})` }} />
          </div>
          <div className="project-item-right-col">
            <div className="project-item-text-wrapper">
              <div className="project-item-heading">{project.title}</div>
              <div className="project-item-period">
                <div className="project-item-period-heading">진행 기간: </div>
                <div>
                  {new Date(project.startDate).toLocaleDateString()}~
                  {new Date(project.endDate).toLocaleDateString()}
                </div>
              </div>
              <div className="project-item-description">
                <div>프로젝트 설명:</div>
                {project.description?.length > 40
                  ? [project.description.slice(0, 40), "..."].join("")
                  : project.description}
              </div>
            </div>
            <div className="project-item-button-wrapper">
              {project.state === "ONGOING" && (
                <Button
                  text={"삭제하기"}
                  onClick={(e) => {
                    handleRemoveProject(e, project.projectId);
                  }}
                />
              )}
              {project.state === "ONGOING" && (
                <Button
                  text={"완료하기"}
                  onClick={(e) => {
                    handleCompleteProject(e, project.projectId);
                  }}
                />
              )}
            </div>
          </div>
        </Link>
      ))}
    </div>
  );
};

export default ProjectList;
