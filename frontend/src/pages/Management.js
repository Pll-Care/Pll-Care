import Button from "../components/common/Button";
import ProjectList from "../components/ProjectManagement/ProjectList";
import NonAuthenticatedManagement from "./NonAuthenticatedManagement";
import Pagination from "../components/common/Pagination";

import { useSelector } from "react-redux";
import { useEffect, useState } from "react";

import { useQuery, useQueryClient } from "react-query";
import { getProjectList } from "../lib/apis/projectManagementApi";
import NewProject from "../components/ProjectManagement/NewProject";

const Management = () => {
  const queryClient = useQueryClient();

  const [currentPage, setCurrentPage] = useState(1);
  const [ongoingCurrentPage, setOngoingCurrentPage] = useState(1);
  const [allProjectListVisible, setAllProjectListVisible] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const authState = useSelector((state) => state.auth.isLoggedIn);

  const [recordDatasPerPage, setRecordDatasPerPage] = useState(4);

  const { data = { projectList: [] } } = useQuery(
    [
      allProjectListVisible
        ? "managementAllProjectList"
        : "managementOngoingProjectList",
      allProjectListVisible ? currentPage : ongoingCurrentPage,
      allProjectListVisible,
    ],
    () =>
      getProjectList(
        allProjectListVisible ? currentPage : ongoingCurrentPage,
        allProjectListVisible ? "ALL" : "ONGOING"
      ),
    { keepPreviousData: true }
  );

  const projectList = data.projectList;

  const lastPageNum = data.totalPages;

  useEffect(() => {
    if (allProjectListVisible) {
      const nextPage = currentPage + 1;

      if (nextPage <= lastPageNum) {
        queryClient.prefetchQuery(
          ["managementAllProjectList", nextPage, allProjectListVisible],
          () => getProjectList(nextPage, "ALL")
        );
      }
    } else {
      const nextPage = ongoingCurrentPage + 1;

      if (nextPage <= lastPageNum) {
        queryClient.prefetchQuery(
          ["managementOngoingProjectList", nextPage, allProjectListVisible],
          () => getProjectList(nextPage, "ONGOING")
        );
      }
    }
  }, [
    allProjectListVisible,
    currentPage,
    lastPageNum,
    ongoingCurrentPage,
    queryClient,
  ]);

  useEffect(() => {
    if (allProjectListVisible) {
      setCurrentPage(1);
    } else {
      setOngoingCurrentPage(1);
    }
  }, [allProjectListVisible]);

  const handleClickAllProjectList = () => {
    setAllProjectListVisible((prevData) => true);
  };

  const handleClickOngoingProjectList = () => {
    setAllProjectListVisible((prevData) => false);
  };

  const handleModalVisible = () => {
    setIsModalVisible(true);
  };

  return (
    <div>
      {authState ? (
        <div className="management">
          <header className="management-main-header">
            <div className="management-main-header-left-col">
              <h1>참여 프로젝트</h1>
              <Button onClick={handleModalVisible} text={"프로젝트 생성"} />
            </div>
            <div className="management-main-header-right-col">
              <Button
                className="all-projects-button"
                text={"전체"}
                type={allProjectListVisible && "positive_dark"}
                onClick={handleClickAllProjectList}
              />
              <Button
                text={"진행중"}
                type={!allProjectListVisible && "positive_dark"}
                onClick={handleClickOngoingProjectList}
              />
            </div>
          </header>
          <main className="project-list-wrapper">
            <div>
              <ProjectList
                type={allProjectListVisible ? "all" : "ongoing"}
                projectList={projectList}
              />
              <Pagination
                currentPage={
                  allProjectListVisible ? currentPage : ongoingCurrentPage
                }
                setCurrentPage={
                  allProjectListVisible ? setCurrentPage : setOngoingCurrentPage
                }
                recordDatasPerPage={recordDatasPerPage}
                totalData={data.totalElements}
              />
            </div>
            {isModalVisible ? (
              <NewProject
                isModalVisible={isModalVisible}
                setIsModalVisible={setIsModalVisible}
              />
            ) : null}
          </main>
        </div>
      ) : (
        <NonAuthenticatedManagement />
      )}
    </div>
  );
};

export default Management;
