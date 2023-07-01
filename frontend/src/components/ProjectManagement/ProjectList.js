import { Link } from "react-router-dom";
import { useState } from "react";

import Button from "../../components/common/Button";
import ProjectButtonModal from "./ProjectButtonModal";
import useManagementMutation from "../../hooks/useManagementMutation";
import ProjectEditor from "./ProjectEditor";

import { getStringDate } from "../../utils/date";
import ProjectItem from "./ProjectItem";

const ProjectList = ({ type, projectList }) => {
  const [deleteModalVisible, setDeleteModalVisible] = useState(false);
  const [deleteProjectId, setDeleteProjectId] = useState();

  const [leaveModalVisible, setLeaveModalVisible] = useState(false);
  const [leaveProjectId, setLeaveProjectId] = useState();

  const [completeModalVisible, setCompleteModalVisible] = useState(false);
  const [completeProjectId, setCompleteProjectId] = useState();

  const [editModalVisible, setEditModalVisible] = useState(false);
  const [editData, setEditData] = useState({
    projectId: 0,
    title: "",
    startDate: new Date(),
    endDate: new Date(),
    imageUrl: "",
    description: "",
  });

  const handleCompleteProjectClick = (e, projectId) => {
    e.preventDefault();

    setCompleteProjectId(projectId);

    setCompleteModalVisible(true);
  };

  const handleDeleteProjectClick = (e, projectId) => {
    e.preventDefault();

    setDeleteProjectId(projectId);

    setDeleteModalVisible(true);
  };

  const handleEditProjectClick = (
    e,
    projectId,
    title,
    startDate,
    endDate,
    description,
    imageUrl
  ) => {
    e.preventDefault();

    setEditModalVisible(true);

    setEditData({
      title,
      projectId,
      startDate: getStringDate(new Date(startDate)),
      endDate: getStringDate(new Date(endDate)),
      description,
      imageUrl,
    });
  };

  const handleLeaveProjectClick = (e, projectId) => {
    e.preventDefault();

    setLeaveProjectId(projectId);

    setLeaveModalVisible(true);
  };

  return (
    <div className="project-list">
      {projectList.length === 0 ? (
        <div className="project-list-description">
          <p>{type === "all" ? "완료된" : "진행 중인"} 프로젝트가 없습니다.</p>
        </div>
      ) : (
        <>
          {projectList?.map((project) => (
            <ProjectItem
              project={project}
              handleCompleteProjectClick={handleCompleteProjectClick}
              handleDeleteProjectClick={handleDeleteProjectClick}
              handleEditProjectClick={handleEditProjectClick}
              handleLeaveProjectClick={handleLeaveProjectClick}
            />
          ))}
          {deleteModalVisible && (
            <ProjectButtonModal
              type={"삭제"}
              projectId={deleteProjectId}
              modalVisible={deleteModalVisible}
              setModalVisible={setDeleteModalVisible}
            />
          )}
          {leaveModalVisible && (
            <ProjectButtonModal
              type={"탈퇴"}
              projectId={leaveProjectId}
              modalVisible={leaveModalVisible}
              setModalVisible={setLeaveModalVisible}
            />
          )}
          {completeModalVisible && (
            <ProjectButtonModal
              type={"완료"}
              projectId={completeProjectId}
              modalVisible={completeModalVisible}
              setModalVisible={setCompleteModalVisible}
            />
          )}
          {editModalVisible && (
            <ProjectEditor
              isModalVisible={editModalVisible}
              setIsModalVisible={setEditModalVisible}
              isEdit={true}
              editData={editData}
            />
          )}
        </>
      )}
    </div>
  );
};

export default ProjectList;
