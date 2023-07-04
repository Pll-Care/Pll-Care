import { useMutation, useQueryClient } from "react-query";
import { customAxios } from "./customAxios";
import { toast } from "react-toastify";

// query key : overviewSchedule
// 오버뷰 일정 가져오는 api 함수
export const getOverviewAllSchedule = async (id) => {
  try {
    const res = await customAxios.get(`/auth/schedule/list?project_id=${id}`);
    return res.data;
  } catch (err) {
    return err;
  }
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
const createNewSchedule = async (data) => {
  try {
    const res = await customAxios.post("/auth/schedule", data);
    return res;
  } catch (err) {
    return err;
  }
};

export const useAddNewScheduleMutation = () => {
  const queryClient = useQueryClient();
  return useMutation(createNewSchedule, {
    onSuccess: () => {
      queryClient.invalidateQueries("calendarSchedule");
      queryClient.invalidateQueries("filterSchedule");
      toast.success("일정이 생성되었습니다");
    },
  });
};

// query key : filterSchedule
// 일정 필터 리스트 조회
export const getFilterSchedule = async (projectId, memberId, type) => {
  try {
    const res = await customAxios.get(
      `/auth/schedule/search?page=1&direction=ASC&sortingProperty=date&projectId=${projectId}&memberId=${memberId}`
    );
    return res.data.content;
  } catch (err) {
    return err;
  }
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
const modifySchedule = async (id, data) => {
  try {
    const res = await customAxios.put(`auth/schedule/${id}`, {
      data: {
        projectId: data.projectId,
        startDate: data.startDate,
        endDate: data.endDate,
        state: data.state,
        memberIds: data.memberIds,
        title: data.title,
        content: data.content,
        category: data.category,
        address: data.category === "MILESTONE" ? "" : data.address,
      },
    });
    return res;
  } catch (err) {
    return err;
  }
};

export const useModifyScheduleMutation = () => {
  const queryClient = useQueryClient();
  return useMutation(modifySchedule, {
    onSuccess: () => {
      queryClient.invalidateQueries("calendarSchedule");
      queryClient.invalidateQueries("filterSchedule");
      toast.success("일정이 수정되었습니다");
    },
  });
};

// 일정 삭제
const deleteSchedule = async (deleteBody) => {
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

export const useDeleteScheduleMutation = () => {
  const queryClient = useQueryClient();
  return useMutation(deleteSchedule, {
    onSuccess: () => {
      queryClient.invalidateQueries("calendarSchedule");
      queryClient.invalidateQueries("filterSchedule");
      toast.success("일정이 삭제되었습니다");
    },
  });
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
