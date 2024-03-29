import { Link } from "react-router-dom";

import { useMediaQuery } from "@mui/material";

import { query, tabletQuery } from "../../utils/mediaQuery";

import projectDefaultImgUrl from "../../assets/project-default-img.jpg";
import leaveImgUrl from "../../assets/leave-button-img.png";

const ProjectItem = ({ project, handleLeaveProjectClick }) => {
  const isMobile = useMediaQuery(query);
  const isTablet = useMediaQuery(tabletQuery);

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
              <span className="project-item-period-heading">진행 기간: </span>
              <span>
                {new Date(project.startDate).toLocaleDateString()}~
                {new Date(project.endDate).toLocaleDateString()}
              </span>
            </div>
            <div className="project-item-description">
              <span className="project-item-description-heading">
                프로젝트 설명:{" "}
              </span>
              {isMobile
                ? project.description?.length > 48
                  ? [project.description.slice(0, 48), "..."].join("")
                  : project.description
                : isTablet
                ? project.description?.length > 120
                  ? [project.description.slice(0, 120), "..."].join("")
                  : project.description
                : project.description?.length > 150
                ? [project.description.slice(0, 150), "..."].join("")
                : project.description}
            </div>
          </div>
          {project.state === "ONGOING" && (
            <div
              className="project-item-button-wrapper"
              onClick={(e) => {
                handleLeaveProjectClick(e, project.projectId);
              }}
            >
              <figure
                style={{
                  backgroundImage: `url(${leaveImgUrl})`,
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
