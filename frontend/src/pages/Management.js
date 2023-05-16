import Button from "../components/Button";
import MainHeader from "../components/MainHeader";
import ProjectList from "../components/ProjectManagement/ProjectList";
import NewProject from "../components/ProjectManagement/NewProject";
import NonAuthenticatedManagement from "./NonAuthenticatedManagement";
import Pagination from "../components/Pagination";

import { useSelector } from "react-redux";
import { useState } from 'react';

const Management = () => {
  const [allProjectListVisible, setAllProjectListVisible] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const projectList = useSelector(state => state.management.projectList);

  const authState = useSelector(state => state.auth.isLoggedIn);

  const [isLoading, setIsLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [recordDatasPerPage, setRecordDatasPerPage] = useState(4);

  const indexOfLast = currentPage * recordDatasPerPage;
  const indexOfFirst = indexOfLast - recordDatasPerPage;

  const getCurrentProjectList = () => {
    return projectList.slice(indexOfFirst, indexOfLast);
  }

  const getOngoingProjectList = () => {
    return projectList.filter((project) => project.state === 'ongoing');
  }

  const getCurrentOngoingProjectList = () => {
    return getOngoingProjectList().slice(indexOfFirst, indexOfLast);
  }

  const handleClickAllProjectList = () => {
    setAllProjectListVisible(true);
  }

  const handleClickOngoingProjectList = () => {
    setAllProjectListVisible(false);
  }

  const handleModalVisible = () => {
    setIsModalVisible(true);
  }

  return (
    <div>
      {authState ? (
        <div className='management'>
          <MainHeader />
          <header className='management-main-header'>
            <div className='management-main-header-left-col'>
              <h1>참여 프로젝트</h1>
              <Button
                onClick={handleModalVisible}
                text={'새 프로젝트 생성'}
              />
            </div>
            <div className='management-main-header-right-col'> 
              <Button
                className='all-projects-button'
                text={'전체'}
                type={allProjectListVisible && 'positive_dark'}
                onClick={handleClickAllProjectList}
              />
              <Button
                text={'진행중'}
                type={!allProjectListVisible && 'positive_dark'}
                onClick={handleClickOngoingProjectList}
              />
            </div>  
          </header>
          <main className='project-list-wrapper'>
            {allProjectListVisible ? (
              <div>
                <ProjectList
                  projectList={getCurrentProjectList()}
                />
                <Pagination 
                  currentPage={currentPage}
                  setCurrentPage={setCurrentPage}
                  recordDatasPerPage={recordDatasPerPage}
                  totalData={projectList?.length}
                />
              </div>
            ) : (
              <div>
                <ProjectList
                    projectList={getCurrentOngoingProjectList()}
                />
                <Pagination 
                  currentPage={currentPage}
                  setCurrentPage={setCurrentPage}
                  recordDatasPerPage={recordDatasPerPage}
                  totalData={getOngoingProjectList()?.length}
                />
              </div>    
            )}
            {isModalVisible ? 
              <NewProject setIsModalVisible={setIsModalVisible} /> :
              null
            }
          </main>
        </div>
      ) : (
        <NonAuthenticatedManagement />
      )}
    </div>
  )
}

export default Management;
