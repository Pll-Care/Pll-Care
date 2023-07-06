import { Link } from "react-router-dom";

import projectDefaultImgUrl from "../../assets/project-default-img.jpg";
import removeImgUrl from "../../assets/remove-button-img.png";
import leaveImgUrl from "../../assets/leave-button-img.png";
import editImgUrl from "../../assets/edit-button-img.png";
import completeImgUrl from "../../assets/complete-button-img.png";

import { Tooltip } from "@mui/material";

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
          <div className="project-item-button-wrapper">
            <div>
              {project.state === "ONGOING" && (
                <Tooltip placement="right" title="삭제하기">
                  <figure
                    style={{
                      backgroundImage: `url(${removeImgUrl})`,
                    }}
                    onClick={(e) =>
                      handleDeleteProjectClick(e, project.projectId)
                    }
                  />
                </Tooltip>
              )}
            </div>
            <div>
              {project.state === "ONGOING" && (
                <Tooltip placement="right" title="수정하기">
                  <figure
                    style={{
                      backgroundImage: `url(${editImgUrl})`,
                    }}
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
                </Tooltip>
              )}
            </div>
            <div>
              {project.state === "ONGOING" && (
                <Tooltip placement="right" title="탈퇴하기">
                  <figure
                    style={{
                      backgroundImage: `url(${leaveImgUrl})`,
                    }}
                    onClick={(e) => {
                      handleLeaveProjectClick(e, project.projectId);
                    }}
                  />
                </Tooltip>
              )}
            </div>
            <div>
              {project.state === "ONGOING" && (
                <Tooltip placement="right" title="완료하기">
                  <figure
                    style={{
                      backgroundImage: `url(${completeImgUrl})`,
                    }}
                    id={project.projectId}
                    onClick={(e) => {
                      handleCompleteProjectClick(e, project.projectId);
                    }}
                  />
                </Tooltip>
              )}
            </div>
          </div>
        </div>
      </Link>
    </div>
  );
};

export default ProjectItem;
