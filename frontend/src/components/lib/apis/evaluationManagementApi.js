import { customAxios } from "./customAxios";

export const getMidEvaluationChartAndRanking = async (projectId) => {
  const response = await customAxios.get(
    `/auth/evaluation/midtermlist?project_id=${projectId}`
  );

  return response.data;
};

export const getEvaluationMember = async (projectId) => {
  const response = await customAxios.get(
    `/auth/evaluation/participant?project_id=${projectId}`
  );

  return response.data;
};
