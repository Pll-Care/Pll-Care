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

// 일정 필터 리스트 조회
export const getFilterSchedule = async (projectId, memberId, category) => {
  try {
    const res = await customAxios.get(
      `/auth/schedule/search?page=1&size=1&direction=ASC&sortingProperty=string&projectId=${projectId}&memberId=${memberId}&scheduleCategory=${category}`
    );
    return res.data.content;
  } catch (err) {
    return err;
  }
};

// 일정 상세 조회
export const getDetailSchedule = async (projectId, scheduleId) => {
  try {
    const res = await customAxios.get(
      `/auth/schedule/${scheduleId}?project_id=${projectId}`
    );
    return res.data;
  } catch (err) {
    return err;
  }
};

// 일정 수정
export const ModifySchedule = async (id, data) => {
  try {
    const res = await customAxios.post(`auth/schedules/${id}`, data);
    return res;
  } catch (err) {
    return err;
  }
};
