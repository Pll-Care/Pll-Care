import { Link } from "react-router-dom";
import { useState } from "react";

import Button from "../../components/common/Button";
import ProjectButtonModal from "./ProjectButtonModal";
import useManagementMutation from "../../hooks/useManagementMutation";
import ProjectEditor from "./ProjectEditor";
import projectDefaultImg from "../../assets/project-default-img.jpg";

import { getStringDate } from "../../utils/date";

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
                    <div className="project-item-period-heading">
                      진행 기간:{" "}
                    </div>
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
