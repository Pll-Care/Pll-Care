import { customAxios } from "./customAxios";

// 달력 모든 일정 가져오는 api 함수
export const getAllSchedule = async (id) => {
  try {
    const res = await customAxios.get(
      `/auth/schedule/calenderlist?project_id=${id}`
    );
    return res.data;
  } catch (err) {
    return err;
  }
};

// 일정 생성하는 함수
export const makeNewPlan = async (data) => {
  try {
    const res = await customAxios.post("/auth/schedule", data);
    return res;
  } catch (err) {
    return err;
  }
};
