import { customAxios } from "./customAxios";

export const getMidEvaluationChartAndRanking = async (projectId) => {
  const response = await customAxios.get(
    `/auth/evaluation/midtermlist?project_id=${projectId}`
  );

  return response.data;
};

export const getFinalEvaluationChartAndRanking = async (projectId) => {
  const response = await customAxios.get(
    `/auth/evaluation/finallist?project_id=${projectId}`
  );

  return response.data;
};

export const getEvaluationMember = async (projectId) => {
  const response = await customAxios.get(
    `/auth/evaluation/participant?project_id=${projectId}`
  );

  return response.data;
};

export const createFinalEvaluation = async (finalEvaluationObj) => {
  const response = await customAxios.post(
    `/auth/evaluation/final`,
    finalEvaluationObj
  );

  return response;
};

// 중간 평가 생성
export const makeNewMidEvaluation = async (data) => {
  try {
    const res = await customAxios.post("/auth/evaluation/midterm", data);
    return res;
  } catch (err) {
    return err;
  }
};
