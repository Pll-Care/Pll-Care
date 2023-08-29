import { customAxios } from "./customAxios";

// query key : overviewSchedule
// 오버뷰 일정 가져오는 api 함수
export const getOverviewAllSchedule = async (id) => {
  const res = await customAxios.get(`/auth/schedule/list?project_id=${id}`);
  return res.data;
};

// query key : calendarSchedule
// 달력 모든 일정 가져오는 api 함수
export const getCalendarAllSchedule = async (id) => {
  const res = await customAxios.get(
    `/auth/schedule/calenderlist?project_id=${id}`
  );
  return res.data;
};

// 일정 생성하는 함수
export const createNewSchedule = async (data) => {
  const res = await customAxios.post("/auth/schedule", data);
  return res;
};

// query key : filterSchedule
// 일정 필터 리스트 조회
export const getFilterSchedule = async (projectId, memberId, type, page) => {
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
};

// todayAfterSchedule
// 오늘 일정
export const getTodayAfterSchedule = async (projectId) => {
  const res = await customAxios.get(
    `/auth/schedule/daily?project_id=${projectId}`
  );
  return res.data;
};

// query key: detailSchedule
// 일정 상세 조회
export const getDetailSchedule = async (projectId, scheduleId) => {
  const res = await customAxios.get(
    `/auth/schedule/${scheduleId}?project_id=${projectId}`
  );
  return res.data;
};

// 일정 수정
export const modifySchedule = async (data) => {
  const { scheduleId, ...formBody } = data;

  const res = await customAxios.put(
    `auth/schedule/${data.scheduleId}`,
    formBody
  );
  return res;
};

// 일정 삭제
export const deleteSchedule = async (deleteBody) => {
  const res = await customAxios.delete(
    `/auth/schedule/${deleteBody.scheduleId}`,
    {
      data: { projectId: deleteBody.projectId },
    }
  );
  return res;
};

// 일정 완료
export const updateDoneSchedule = async (data) => {
  const { scheduleId, ...body } = data;

  const res = await customAxios.post(
    `/auth/schedule/${data.scheduleId}/state`,
    body
  );
  return res;
};

// 일정 완료
export const updateDoneShcedule = async (scheduleId, projectId) => {
  const body = {
    projectId: projectId,
    state: "TBD",
  };
  const res = await customAxios.post(
    `/auth/schedule/${scheduleId}/state`,
    body
  );
  return res;
};
