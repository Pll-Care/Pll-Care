import { createContext, useContext } from "react";

const ManagementContext = createContext(null);
export const useManagementClient = () => useContext(ManagementContext);

const ManagementClientProvider = ({ children, managementService }) => {
  // managementApi
  const getProjectList =
    managementService.getProjectList.bind(managementService);
  const createProject = managementService.createProject.bind(managementService);
  const leaveProject = managementService.leaveProject.bind(managementService);
  const getCompleteProjectData =
    managementService.getCompleteProjectData.bind(managementService);
  const getIsLeaderData =
    managementService.getIsLeaderData.bind(managementService);
  // evaluationManagementApi
  const getMidEvaluationChartAndRanking =
    managementService.getMidEvaluationChartAndRanking.bind(managementService);
  const getFinalEvaluationChartAndRanking =
    managementService.getFinalEvaluationChartAndRanking.bind(managementService);
  const getEvaluationMember =
    managementService.getEvaluationMember.bind(managementService);
  const createFinalEvaluation =
    managementService.createFinalEvaluation.bind(managementService);
  const getFinalEvaluation =
    managementService.getFinalEvaluation.bind(managementService);
  // meetingRecordManagementApi
  const getAllMeetingRecordList =
    managementService.getAllMeetingRecordList.bind(managementService);
  const getBookMarkMeetingRecordList =
    managementService.getBookMarkMeetingRecordList.bind(managementService);
  const createMeetingRecord =
    managementService.createMeetingRecord.bind(managementService);
  const getMeetingRecord =
    managementService.getMeetingRecord.bind(managementService);
  const deleteMeetingRecord =
    managementService.deleteMeetingRecord.bind(managementService);
  const createBookMarkMeetingRecord =
    managementService.createBookMarkMeetingRecord.bind(managementService);
  const editMeetingRecord =
    managementService.editMeetingRecord.bind(managementService);
  // projectManagementApi
  const deleteProject = managementService.deleteProject.bind(managementService);
  const editProject = managementService.editProject.bind(managementService);
  const completeProject =
    managementService.completeProject.bind(managementService);
  const getProjectData =
    managementService.getProjectData.bind(managementService);

  return (
    <ManagementContext.Provider
      value={{
        getProjectList,
        createProject,
        leaveProject,
        getCompleteProjectData,
        getIsLeaderData,
        getMidEvaluationChartAndRanking,
        getFinalEvaluationChartAndRanking,
        getEvaluationMember,
        createFinalEvaluation,
        getFinalEvaluation,
        getAllMeetingRecordList,
        getBookMarkMeetingRecordList,
        createMeetingRecord,
        getMeetingRecord,
        deleteMeetingRecord,
        createBookMarkMeetingRecord,
        editMeetingRecord,
        deleteProject,
        editProject,
        completeProject,
        getProjectData,
      }}
    >
      {children}
    </ManagementContext.Provider>
  );
};

export default ManagementClientProvider;
