import { createContext, useReducer, useRef, useState } from "react";
import Button from "../components/Button";
import MainHeader from "../components/MainHeader";
import ProjectList from "../components/ProjectManagement/ProjectList";
import NewProject from "../components/ProjectManagement/NewProject";

const projectListReducer = (state, action) => {
  switch (action.type) {
    case 'CREATE': {
      return [...state, action.data];
    }
    case 'REMOVE': {
      return state.filter((item) => parseInt(item.id) !== parseInt(action.targetId));
    }
    case 'COMPLETE': {
      return state.map((item) => parseInt(item.id) === parseInt(action.targetId) ? { ...item, state: 'complete' } : item);
    }
    default: {
      return state;
    }
  }
}

export const ProjectListDispatchContext = createContext();

const Management = () => {
  const [allProjectListVisible, setAllProjectListVisible] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const [projectList, projectListDispatch] = useReducer(projectListReducer, []);

  const idRef = useRef(1);

  const getOngoingProjectList = () => {
    return projectList.filter((project) => project.state === 'ongoing');
  }
  
  const ongoingProjectList = getOngoingProjectList();

  const handleClickAllProjectList = () => {
    setAllProjectListVisible(true);
  }

  const handleClickOngoingProjectList = () => {
    setAllProjectListVisible(false);
  }

  const handleModalVisible = () => {
    setIsModalVisible(true);
  }

  const onCreateProject = (startDate, endDate, title, description, state) => {
    projectListDispatch({
      type: 'CREATE',
      data: {
        id: idRef.current,
        startDate,
        endDate,
        title,
        description,
        state,
      }
    });

    idRef.current += 1;
  }

  const onRemoveProject = (targetId) => {
    projectListDispatch({
      type: 'REMOVE',
      targetId
    });
  }

  const onCompleteProject = (targetId) => {
    projectListDispatch({
      type: 'COMPLETE',
      targetId
    })
  }

  return (
    <ProjectListDispatchContext.Provider value={{ onCreateProject, onRemoveProject, onCompleteProject }}>
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
          {allProjectListVisible ?
            <ProjectList
              projectList={projectList}
            /> :
            <ProjectList
              projectList={ongoingProjectList}
            />
          }
          {isModalVisible ? 
            <NewProject setIsModalVisible={setIsModalVisible} /> :
            null
          }
        </main>
      </div>
    </ProjectListDispatchContext.Provider>
  )
}

export default Management;
