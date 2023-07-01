export const isCompleteProject = (completeProjectList, projectId) => {
  return completeProjectList.includes(projectId) ? "COMPLETE" : "ONGOING";
};
