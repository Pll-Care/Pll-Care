import Button from "../components/common/Button";
import ProjectList from "../components/Management/ProjectList";
import Pagination from "../components/common/Pagination";
import NewProject from "../components/Management/NewProject";

import { useSelector } from "react-redux";
import { useEffect, useState } from "react";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";

import { history } from "../utils/history";

import { getProjectList } from "../lib/apis/managementApi";

const Management = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [ongoingCurrentPage, setOngoingCurrentPage] = useState(1);
  const [allProjectListVisible, setAllProjectListVisible] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const authState = useSelector((state) => state.auth.isLoggedIn);

  const navigate = useNavigate();

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

  useEffect(() => {
    const listenBackEvent = () => {
      navigate("/management");
    };

    const historyEvent = history.listen(({ action }) => {
      if (action === "POP") {
        listenBackEvent();
      }
    });

    return historyEvent;
  }, [navigate]);

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
    </div>
  );
};

export default Management;
