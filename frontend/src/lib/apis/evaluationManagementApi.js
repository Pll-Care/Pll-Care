import { customAxios } from "./customAxios";
import { toast } from "react-toastify";

export const getMidEvaluationChartAndRanking = async (projectId) => {
  try {
    const response = await customAxios.get(
      `/auth/evaluation/midtermlist?project_id=${projectId}`
    );

    return response.data;
  } catch (e) {
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004"
    ) {
      toast.error(e.response.data.message);
    }
  }
};

export const getFinalEvaluationChartAndRanking = async (projectId) => {
  try {
    const response = await customAxios.get(
      `/auth/evaluation/finallist?project_id=${projectId}`
    );

    return response.data;
  } catch (e) {
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004"
    ) {
      toast.error(e.response.data.message);
    }
  }
};

export const getEvaluationMember = async (projectId) => {
  try {
    const response = await customAxios.get(
      `/auth/evaluation/participant?project_id=${projectId}`
    );

    return response.data;
  } catch (e) {
    if (
      e.response.data.code === "PROJECT_001" ||
      e.response.data.code === "PROJECT_004"
    ) {
      toast.error(e.response.data.message);
    }
  }
};

export const createFinalEvaluation = async (finalEvaluationObj) => {
  try {
    const response = await customAxios.post(
      `/auth/evaluation/final`,
      finalEvaluationObj
    );

    toast.success("최종 평가되었습니다!");

    return response;
  } catch (e) {
    if (
      e.response.data.code === "MEMBER_001" ||
      e.response.data.code === "PROJECT_002" ||
      e.response.data.code === "PROJECT_015" ||
      e.response.data.code === "EVAL_002" ||
      e.response.data.code === "EVAL_006" ||
      e.response.data.code === "EVAL_007"
    ) {
      toast.error(e.response.data.message);
    }
  }
};

export const getFinalEvaluation = async (finalEvalId, projectId) => {
  try {
    const response = await customAxios.get(
      `/auth/evaluation/final/${finalEvalId}?project_id=${projectId}`
    );

    return response.data;
  } catch (e) {
    if (e.response.data.code === "EVAL_001") {
      toast.error(e.response.data.message);
    }
  }
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
