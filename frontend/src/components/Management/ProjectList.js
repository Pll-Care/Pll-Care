import { useState } from "react";

import ProjectButtonModal from "./ProjectButtonModal";
import ProjectItem from "./ProjectItem";

const ProjectList = ({ type, projectList, totalElements }) => {
  const [leaveModalVisible, setLeaveModalVisible] = useState(false);
  const [leaveProjectId, setLeaveProjectId] = useState();

  const handleLeaveProjectClick = (e, projectId) => {
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
              handleLeaveProjectClick={handleLeaveProjectClick}
            />
          ))}
          {leaveModalVisible && (
            <ProjectButtonModal
              type={"팀 탈퇴"}
              projectId={leaveProjectId}
              modalVisible={leaveModalVisible}
              setModalVisible={setLeaveModalVisible}
            />
          )}
        </>
      )}
    </div>
  );
};

export default ProjectList;
