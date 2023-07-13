import { Link } from "react-router-dom";

import projectDefaultImgUrl from "../../assets/project-default-img.jpg";
import leaveImgUrl from "../../assets/leave-button-img.png";

const ProjectItem = ({ project, handleLeaveProjectClick }) => {
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
                project.imageUrl ? project.imageUrl : projectDefaultImgUrl
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
          {project.state === "ONGOING" && (
            <div className="project-item-button-wrapper">
              <figure
                style={{
                  backgroundImage: `url(${leaveImgUrl})`,
                }}
                onClick={(e) => {
                  handleLeaveProjectClick(e, project.projectId);
                }}
              />
              <div className="leave-button-text">팀 나가기</div>
            </div>
          )}
        </div>
      </Link>
    </div>
  );
};

export default ProjectItem;
