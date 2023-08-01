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
        meetingRecordManagementActions.setSelectedMeetingRecordState(false)
      );
      dispatch(
        meetingRecordManagementActions.setIsCreatedMeetingRecordVisibleState(
          true
        )
      );
      dispatch(meetingRecordManagementActions.setCreatedMeetingRecordId(data));
      dispatch(meetingRecordManagementActions.setIsEditState(false));
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      queryClient.invalidateQueries(["managementCreatedMeetingRecordList"]);
      toast.success("작성 완료되었습니다!");
    },
    onError: () => {
      toast.error("생성 실패하였습니다. 잠시 후 다시 시도해주세요.");
    },
  });

  const { mutate: deleteMutate } = useMutation(deleteMeetingRecord, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
      toast.success("삭제되었습니다!");
    },
    onError: () => {
      toast.error("삭제 실패하였습니다. 잠시 후 다시 시도해주세요.");
    },
  });

  const { mutate: editMutate } = useMutation(editMeetingRecord, {
    onSuccess: (data) => {
      dispatch(
        meetingRecordManagementActions.setSelectedMeetingRecordState(false)
      );
      dispatch(
        meetingRecordManagementActions.setIsCreatedMeetingRecordVisibleState(
          true
        )
      );
      dispatch(meetingRecordManagementActions.setCreatedMeetingRecordId(data));
      dispatch(meetingRecordManagementActions.setIsEditState(false));
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
      queryClient.invalidateQueries(["managementCreatedMeetingRecordList"]);
      toast.success("수정되었습니다!");
    },
    onError: () => {
      toast.error("수정 실패하였습니다. 잠시 후 다시 시도해주세요.");
    },
  });

  const { mutate: createBookMarkMutate } = useMutation(
    createBookMarkMeetingRecord,
    {
      onSuccess: () => {
        queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
        toast.success("북마크/북마크 취소되었습니다!");
      },
      onError: () => {
        toast.error(
          "북마크/북마크 취소 실패하였습니다. 잠시 후 다시 시도해주세요."
        );
      },
    }
  );

  return { createMutate, deleteMutate, editMutate, createBookMarkMutate };
};

export default useMeetingRecordManagementMutation;
