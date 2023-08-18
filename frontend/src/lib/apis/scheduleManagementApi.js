import { customAxios } from "./customAxios";

// query key : overviewSchedule
// 오버뷰 일정 가져오는 api 함수
export const getOverviewAllSchedule = async (id) => {
  try {
    const res = await customAxios.get(`/auth/schedule/list?project_id=${id}`);
    return res.data;
  } catch (err) {}
};

// query key : calendarSchedule
// 달력 모든 일정 가져오는 api 함수
export const getCalendarAllSchedule = async (id) => {
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
export const createNewSchedule = async (data) => {
  try {
    const res = await customAxios.post("/auth/schedule", data);
    return res;
  } catch (err) {
    return err;
  }
};

// query key : filterSchedule
// 일정 필터 리스트 조회
export const getFilterSchedule = async (projectId, memberId, type, page) => {
  try {
    if (type === "all") {
      const res = await customAxios.get(
        `/auth/schedule/search?page=${page}&size=5&projectId=${projectId}&memberId=${memberId}`
      );
      return res.data;
    }
    if (type === "pastAll") {
      const res = await customAxios.get(
        `/auth/schedule/search?page=${page}&size=5&projectId=${projectId}&memberId=${memberId}&previous=true`
      );
      return res.data;
    }
    const res = await customAxios.get(
      `/auth/schedule/search?page=${page}&size=5&projectId=${projectId}&memberId=${memberId}&scheduleCategory=${type}`
    );
    return res.data;
  } catch (err) {
    return err;
  }
};

// todayAfterSchedule
// 오늘 이후 일정
export const getTodayAfterSchedule = async (projectId) => {
  try {
    const res = await customAxios.get(
      `/auth/schedule/daily?project_id=${projectId}`
    );
    return res.data;
  } catch (err) {}
};

// query key: detailSchedule
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
export const modifySchedule = async (data) => {
  const { scheduleId, ...formBody } = data;

  try {
    const res = await customAxios.put(
      `auth/schedule/${data.scheduleId}`,
      formBody
    );
    return res;
  } catch (err) {
    return err;
  }
};

// 일정 삭제
export const deleteSchedule = async (deleteBody) => {
  try {
    const res = await customAxios.delete(
      `/auth/schedule/${deleteBody.scheduleId}`,
      {
        data: { projectId: deleteBody.projectId },
      }
    );
    return res;
  } catch (err) {
    return err;
  }
};

// 일정 완료
export const updateDoneSchedule = async (data) => {
  const { scheduleId, ...body } = data;
  try {
    const res = await customAxios.post(
      `/auth/schedule/${data.scheduleId}/state`,
      body
    );
    return res;
  } catch (err) {
    return err;
  }
};

// 일정 완료
export const updateDoneShcedule = async (scheduleId, projectId) => {
  try {
    const body = {
      projectId: projectId,
      state: "TBD",
    };
    const res = await customAxios.post(
      `/auth/schedule/${scheduleId}/state`,
      body
    );
    return res;
  } catch (err) {
    return err;
  }
};
