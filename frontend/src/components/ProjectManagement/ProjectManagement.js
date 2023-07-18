import { useState } from "react";

import { getStringDate } from "../../utils/date";

import projectButtonImgUrl from "../../assets/project-management-img.png";

import ProjectButtonModal from "../Management/ProjectButtonModal";
import ProjectEditor from "../Management/ProjectEditor";
import { getProjectId } from "../../utils/getProjectId";
import { useLocation } from "react-router-dom";
import { useQuery } from "react-query";
import { getProjectData } from "../../lib/apis/projectManagementApi";

const ProjectManagement = () => {
  const projectId = getProjectId(useLocation());

  const { data } = useQuery(["managementProject", projectId], () =>
    getProjectData(projectId)
  );

  const [deleteModalVisible, setDeleteModalVisible] = useState(false);
  const [deleteProjectId, setDeleteProjectId] = useState();

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

  const handleEditProjectClick = (e) => {
    e.preventDefault();

    setEditModalVisible(true);

    setEditData({
      title: data.title,
      projectId: projectId,
      startDate: getStringDate(new Date(data.startDate)),
      endDate: getStringDate(new Date(data.endDate)),
      description: data.description,
      imageUrl: data.imageUrl,
    });
  };

  return (
    <div className="project-management">
      <div className="project-button-wrapper">
        <figure
          style={{
            backgroundImage: `url(${projectButtonImgUrl})`,
          }}
          onClick={(e) => handleCompleteProjectClick(e, projectId)}
        />
        <h1>프로젝트 완료</h1>
      </div>
      <div className="project-button-wrapper">
        <figure
          style={{
            backgroundImage: `url(${projectButtonImgUrl})`,
          }}
          onClick={(e) => handleDeleteProjectClick(e, projectId)}
        />
        <h1>프로젝트 삭제</h1>
      </div>
      <div className="project-button-wrapper">
        <figure
          style={{
            backgroundImage: `url(${projectButtonImgUrl})`,
          }}
          onClick={(e) => handleEditProjectClick(e)}
        />
        <h1>프로젝트 수정</h1>
      </div>
      {completeModalVisible && (
        <ProjectButtonModal
          type={"완료"}
          projectId={completeProjectId}
          modalVisible={completeModalVisible}
          setModalVisible={setCompleteModalVisible}
        />
      )}
      {deleteModalVisible && (
        <ProjectButtonModal
          type={"삭제"}
          projectId={deleteProjectId}
          modalVisible={deleteModalVisible}
          setModalVisible={setDeleteModalVisible}
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
    </div>
  );
};

export default ProjectManagement;