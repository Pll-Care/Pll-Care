import { Link } from "react-router-dom";

import projectDefaultImg from "../../assets/project-default-img.jpg";
import Button from "../common/Button";

const ProjectItem = ({
  project,
  handleCompleteProjectClick,
  handleDeleteProjectClick,
  handleEditProjectClick,
  handleLeaveProjectClick,
}) => {
    console.log(project.state);
  return (
    <div>
      <Link
        className="project-item"
        key={project.projectId}
        to={`/management/${project.projectId}/overview`}
      >
        <div className="project-item-left-col">
          <figure
            style={{
              backgroundImage: `url(${
                project.imageUrl ? project.imageUrl : projectDefaultImg
              })`,
            }}
          />
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
            <div>
              {project.state === "ONGOING" && (
                <Button
                  text={"삭제하기"}
                  onClick={(e) =>
                    handleDeleteProjectClick(e, project.projectId)
                  }
                />
              )}
              {project.state === "ONGOING" && (
                <Button
                  text={"수정하기"}
                  onClick={(e) => {
                    handleEditProjectClick(
                      e,
                      project.projectId,
                      project.title,
                      project.startDate,
                      project.endDate,
                      project.description,
                      project.imageUrl
                    );
                  }}
                />
              )}
            </div>
            <div>
              {project.state === "ONGOING" && (
                <Button
                  text={"탈퇴하기"}
                  onClick={(e) => {
                    handleLeaveProjectClick(e, project.projectId);
                  }}
                />
              )}
              {project.state === "ONGOING" && (
                <Button
                  text={"완료하기"}
                  id={project.projectId}
                  onClick={(e) => {
                    handleCompleteProjectClick(e, project.projectId);
                  }}
                />
              )}
            </div>
          </div>
        </div>
      </Link>
    </div>
  );
};

export default ProjectItem;
