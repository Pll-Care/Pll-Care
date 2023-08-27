import { customAxios } from "./customAxios";
import { toast } from "react-toastify";

export const getMidEvaluationChartAndRanking = async (projectId) => {
  try {
    const response = await customAxios.get(
      `/auth/evaluation/midtermlist?project_id=${projectId}`
    );
  
    return response.data;
  } catch (e) {
    if (e.response.data.code === "PROJECT_001") {
      toast.error("프로젝트를 찾을 수 없습니다.");
    } else if (e.response.data.code === "PROJECT_004") {
      toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
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
    if (e.response.data.code === "PROJECT_001") {
      toast.error("프로젝트를 찾을 수 없습니다.");
    } else if (e.response.data.code === "PROJECT_004") {
      toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
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
    if (e.response.data.code === "PROJECT_001") {
      toast.error("프로젝트를 찾을 수 없습니다.");
    } else if (e.response.data.code === "PROJECT_004") {
      toast.error("해당 프로젝트에 대한 접근 권한이 없습니다.");
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
    if (e.response.data.code === "MEMBER_001") {
      toast.error("사용자를 찾을 수 없습니다.");
    } else if (e.response.data.code === "PROJECT_002") {
      toast.error("프로젝트에서 해당 사용자를 찾을 수 없습니다.");
    } else if (e.response.data.code === "PROJECT_015") {
      toast.error("완료되지 않은 프로젝트입니다.");
    } else if (e.response.data.code === "EVAL_002") {
      toast.error("최종 평가 점수가 범위를 벗어났습니다.");
    } else if (e.response.data.code === "EVAL_006") {
      toast.error("자기 자신의 평가를 할 수 없습니다.");
    } else if (e.response.data.code === "EVAL_007") {
      toast.error("동일한 평가를 다시 할 수 없습니다.");
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
      toast.error("평가를 찾을 수 없습니다.");
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
