import { useState } from "react";
import { useLocation } from "react-router-dom";
import { useQuery } from "react-query";

import projectButtonImgUrl from "../../assets/project-management-img.png";
import completedProjectButtonImgUrl from "../../assets/completed-project-management-img.png";

import ProjectEditor from "../Management/ProjectEditor";
import AlertCheckModal from "../common/Modal/AlertCheckModal";

import useManagementMutation from "../../hooks/Mutations/useManagementMutation";
import { getAlertText } from "../../utils/getAlertText";
import { getStringDate } from "../../utils/date";
import { getProjectId } from "../../utils/getProjectId";
import { useManagementClient } from "../../context/Client/ManagementClientContext";

const ProjectManagement = () => {
  const projectId = getProjectId(useLocation());

  const { getProjectData, getCompleteProjectData } = useManagementClient();

  const { data } = useQuery(["managementProject", projectId], () =>
    getProjectData(projectId)
  );

  const { data: isCompleted } = useQuery(
    ["completeProjectData", projectId],
    () => getCompleteProjectData(projectId)
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

  const { deleteMutate, completeMutate } = useManagementMutation();

  return (
    <div className="project-management">
      {isCompleted ? (
        <div className="completed-project">
          <div className="project-button-wrapper">
            <figure
              style={{
                backgroundImage: `url(${completedProjectButtonImgUrl})`,
              }}
              onClick={(e) => handleCompleteProjectClick(e, projectId)}
            />
            <h1>프로젝트 완료</h1>
          </div>
          <div className="project-button-wrapper">
            <figure
              style={{
                backgroundImage: `url(${completedProjectButtonImgUrl})`,
              }}
              onClick={(e) => handleDeleteProjectClick(e, projectId)}
            />
            <h1>프로젝트 삭제</h1>
          </div>
          <div className="project-button-wrapper">
            <figure
              style={{
                backgroundImage: `url(${completedProjectButtonImgUrl})`,
              }}
              onClick={(e) => handleEditProjectClick(e)}
            />
            <h1>프로젝트 수정</h1>
          </div>
          {completeModalVisible && (
            <AlertCheckModal
              text={getAlertText("완료")}
              onClose={() => setCompleteModalVisible(false)}
              open={completeModalVisible}
              clickHandler={() => {
                completeMutate(completeProjectId);
              }}
              width={380}
              height={210}
            />
          )}
          {deleteModalVisible && (
            <AlertCheckModal
              text={getAlertText("삭제")}
              onClose={() => setDeleteModalVisible(false)}
              open={deleteModalVisible}
              clickHandler={() => {
                deleteMutate(deleteProjectId);
              }}
              width={380}
              height={210}
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
      ) : (
        <div className="ongoing-project">
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
            <AlertCheckModal
              text={getAlertText("완료")}
              onClose={() => setCompleteModalVisible(false)}
              open={completeModalVisible}
              clickHandler={() => {
                completeMutate(completeProjectId);
              }}
              width={380}
              height={210}
            />
          )}
          {deleteModalVisible && (
            <AlertCheckModal
              text={getAlertText("삭제")}
              onClose={() => setDeleteModalVisible(false)}
              open={deleteModalVisible}
              clickHandler={() => {
                deleteMutate(deleteProjectId);
              }}
              width={380}
              height={210}
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
      )}
    </div>
  );
};

export default ProjectManagement;
