import { customAxios } from "./customAxios";

// 중간 평가 생성
export const makeNewMidEvaluation = async (data) => {
  const res = await customAxios.post("/auth/evaluation/midterm", data);
  return res;
};
