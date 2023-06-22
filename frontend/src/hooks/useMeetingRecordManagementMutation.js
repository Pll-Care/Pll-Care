import { useMutation, useQueryClient } from "react-query";

import {
  createBookMarkMeetingRecord,
  createMeetingRecord,
  deleteMeetingRecord,
  editMeetingRecord,
} from "../lib/apis/meetingRecordManagementApi";
import { useDispatch } from "react-redux";
import { meetingRecordManagementActions } from "../redux/meetingRecordManagementSlice";
import { toast } from "react-toastify";

const useMeetingRecordManagementMutation = () => {
  const queryClient = useQueryClient();

  const dispatch = useDispatch();

  const { mutate: createMutate } = useMutation(createMeetingRecord, {
    onSuccess: (data) => {
      dispatch(
        meetingRecordManagementActions.onEditCreatedMeetingRecordId(data)
      );
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      toast.success("작성 완료되었습니다!");
    },
  });

  const { mutate: deleteMutate } = useMutation(deleteMeetingRecord, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
      toast.success("삭제되었습니다!");
    },
  });

  const { mutate: editMutate } = useMutation(editMeetingRecord, {
    onSuccess: (data) => {
      dispatch(
        meetingRecordManagementActions.onEditCreatedMeetingRecordId(data)
      );
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
      toast.success("수정되었습니다!");
    },
  });

  const { mutate: createBookMarkMutate } = useMutation(
    createBookMarkMeetingRecord,
    {
      onSuccess: () => {
        queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
        toast.success("북마크/북마크 취소되었습니다!");
      },
    }
  );

  return { createMutate, deleteMutate, editMutate, createBookMarkMutate };
};

export default useMeetingRecordManagementMutation;