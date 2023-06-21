import Button from '../components/common/Button';
import ProjectList from "../components/ProjectManagement/ProjectList";
import NewProject from "../components/ProjectManagement/NewProject";
import NonAuthenticatedManagement from "./NonAuthenticatedManagement";
import Pagination from "../components/common/Pagination";

import { useSelector } from "react-redux";
import { useEffect, useState } from 'react';

import { useQuery, useQueryClient } from 'react-query';
import { getProjectList } from "../lib/apis/projectManagementApi";

const Management = () => {
  const queryClient = useQueryClient();

  const [currentPage, setCurrentPage] = useState(1);
  const [ongoingCurrentPage, setOngoingCurrentPage] = useState(1);
  const [allProjectListVisible, setAllProjectListVisible] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const authState = useSelector(state => state.auth.isLoggedIn);

  const [recordDatasPerPage, setRecordDatasPerPage] = useState(4);

  const { data = { projectList: [] } } = useQuery(['managementProjectList', currentPage, allProjectListVisible], () => getProjectList(currentPage));

  const { data: ongoingData = { projectList: [] } } = useQuery(['managementProjectList', ongoingCurrentPage, allProjectListVisible], () => getProjectList(ongoingCurrentPage, 'ONGOING'));

  const projectList = data.projectList;

  const lastPageNum = data.totalPages;

  const ongoingProjectList = ongoingData.projectList;

  const ongoingLastPageNum = ongoingData.totalPages;

  useEffect(() => {
    if (allProjectListVisible) {
      const nextPage = currentPage + 1;

      if (nextPage <= lastPageNum) {
        queryClient.prefetchQuery(['managementProjectList', nextPage, allProjectListVisible], () => getProjectList(nextPage));
      }
    } else {
      const ongoingNextPage = ongoingCurrentPage + 1;

      if (ongoingNextPage <= ongoingLastPageNum) {
        queryClient.prefetchQuery(['managementProjectList', ongoingNextPage, allProjectListVisible], () => getProjectList(ongoingNextPage, 'ONGOING'));
      }
    }
  }, [allProjectListVisible, currentPage, lastPageNum, ongoingCurrentPage, ongoingLastPageNum, queryClient]);

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
          <header className='management-main-header'>
            <div className='management-main-header-left-col'>
              <h1>참여 프로젝트</h1>
              <Button
                onClick={handleModalVisible}
                text={'프로젝트 생성'}
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
                  projectList={projectList}
                />
                <Pagination 
                  currentPage={currentPage}
                  setCurrentPage={setCurrentPage}
                  recordDatasPerPage={recordDatasPerPage}
                  totalData={data.totalElements}
                />
              </div>
            ) : (
              <div>
                <ProjectList
                  projectList={ongoingProjectList}
                />
                <Pagination 
                  currentPage={ongoingCurrentPage}
                  setCurrentPage={setOngoingCurrentPage}
                  recordDatasPerPage={recordDatasPerPage}
                  totalData={ongoingData.totalElements}
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
