import Button from "../components/common/Button";
import ProjectList from "../components/Management/ProjectList";
import Pagination from "../components/common/Pagination";
import NewProject from "../components/Management/NewProject";

import { useEffect, useState } from "react";
import { useQuery } from "react-query";

import { getProjectList } from "../lib/apis/managementApi";

const Management = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [ongoingCurrentPage, setOngoingCurrentPage] = useState(1);
  const [allProjectListVisible, setAllProjectListVisible] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);

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

  const totalElements = data.totalElements;

  useEffect(() => {
    if (allProjectListVisible) {
      setCurrentPage(1);
    } else {
      setOngoingCurrentPage(1);
    }
  }, [allProjectListVisible]);

  // useEffect(() => {
  //   const listenBackEvent = () => {
  //     navigate("/management");
  //   };

  //   const historyEvent = history.listen(({ action }) => {
  //     if (action === "POP") {
  //       listenBackEvent();
  //     }
  //   });

  //   return historyEvent;
  // }, [navigate]);

  const handleClickAllProjectList = () => {
    setAllProjectListVisible(true);
  };

  const handleClickOngoingProjectList = () => {
    setAllProjectListVisible(false);
  };

  const handleModalVisible = () => {
    setIsModalVisible(true);
  };

  return (
    <div>
      <div className="management">
        <header className="management-main-header">
          <div className="management-main-header-left-col">
            <h1>참여 프로젝트</h1>
            <Button
              onClick={handleModalVisible}
              color={"gray"}
              text={"프로젝트 생성"}
            />
          </div>
          <div className="management-main-header-right-col">
            <Button
              className="all-projects-button"
              text={"전체"}
              color={"gray"}
              type={allProjectListVisible && "positive_dark"}
              onClick={handleClickAllProjectList}
            />
            <Button
              text={"진행중"}
              color={"gray"}
              type={!allProjectListVisible && "positive_dark"}
              onClick={handleClickOngoingProjectList}
            />
          </div>
        </header>
        <main className="project-list-wrapper">
          <div>
            <ProjectList
              type={allProjectListVisible ? "all" : "ongoing"}
              totalElements={totalElements}
              projectList={projectList}
            />
            <Pagination
              currentPage={
                allProjectListVisible ? currentPage : ongoingCurrentPage
              }
              setCurrentPage={
                allProjectListVisible ? setCurrentPage : setOngoingCurrentPage
              }
              totalPages={data.totalPages}
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
    </div>
  );
};

export default Management;
