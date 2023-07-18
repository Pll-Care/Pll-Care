import { useMutation, useQueryClient } from "react-query";
import {
  createNewSchedule,
  deleteSchedule,
  modifySchedule,
  updateDoneSchedule,
} from "../lib/apis/scheduleManagementApi";
import { toast } from "react-toastify";

// 일정 생성
export const useAddNewScheduleMutation = () => {
  const queryClient = useQueryClient();
  return useMutation(createNewSchedule, {
    onSuccess: () => {
      queryClient.invalidateQueries("calendarSchedule");
      queryClient.invalidateQueries("filterSchedule");
      queryClient.invalidateQueries("overviewSchedule");
      toast.success("일정이 생성되었습니다");
    },
  });
};

// 일정 수정
export const useModifyScheduleMutation = () => {
  const queryClient = useQueryClient();
  return useMutation(modifySchedule, {
    onSuccess: () => {
      queryClient.invalidateQueries("calendarSchedule");
      queryClient.invalidateQueries("filterSchedule");
      queryClient.invalidateQueries("overviewSchedule");
      queryClient.invalidateQueries("scheduleDetail");
      toast.success("일정이 수정되었습니다");
    },
  });
};

// 일정 삭제
export const useDeleteScheduleMutation = () => {
  const queryClient = useQueryClient();
  return useMutation(deleteSchedule, {
    onSuccess: () => {
      queryClient.invalidateQueries("calendarSchedule");
      queryClient.invalidateQueries("filterSchedule");
      queryClient.invalidateQueries("overviewSchedule");
      toast.success("일정이 삭제되었습니다");
    },
  });
};

// 일정 완료
export const useCompleteScheduleMutation = () => {
  const queryClient = useQueryClient();
  return useMutation(updateDoneSchedule, {
    onSuccess: () => {
      queryClient.invalidateQueries("filterSchedule");
      toast.success("일정이 완료처리되었습니다");
    },
  });
};
