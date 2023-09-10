import { useState } from "react";

import ProjectItem from "./ProjectItem";
import AlertCheckModal from "../common/Modal/AlertCheckModal";
import { getAlertText } from "../../utils/getAlertText";
import useManagementMutation from "../../hooks/Mutations/useManagementMutation";

const ProjectList = ({ type, projectList, totalElements }) => {
  const [leaveModalVisible, setLeaveModalVisible] = useState(false);
  const [leaveProjectId, setLeaveProjectId] = useState();

  const { leaveMutate } = useManagementMutation();

  const handleLeaveButtonClick = (e, projectId) => {
    e.preventDefault();

    setLeaveProjectId(projectId);

    setLeaveModalVisible(true);
  };

  return (
    <div className="project-list">
      {totalElements === 0 ? (
        <div className="project-list-description">
          <p>{type === "all" ? "완료된" : "진행 중인"} 프로젝트가 없습니다.</p>
        </div>
      ) : (
        <>
          {projectList?.map((project) => (
            <ProjectItem
              key={project.projectId}
              project={project}
              handleLeaveProjectClick={(e) =>
                handleLeaveButtonClick(e, project.projectId)
              }
            />
          ))}
          {leaveModalVisible && (
            <AlertCheckModal
              text={getAlertText("팀 탈퇴")}
              onClose={() => setLeaveModalVisible(false)}
              open={leaveModalVisible}
              clickHandler={() => leaveMutate(leaveProjectId)}
              width={500}
            />
          )}
        </>
      )}
    </div>
  );
};

export default ProjectList;
