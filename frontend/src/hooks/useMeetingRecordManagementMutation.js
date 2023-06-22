import { useMutation, useQueryClient } from "react-query";

import {
  createBookMarkMeetingRecord,
  createMeetingRecord,
  deleteMeetingRecord,
  editMeetingRecord,
} from "../lib/apis/meetingRecordManagementApi";
import { useDispatch } from "react-redux";
import { meetingRecordManagementActions } from "../redux/meetingRecordManagementSlice";

const useMeetingRecordManagementMutation = () => {
  const queryClient = useQueryClient();

  const dispatch = useDispatch();

  const { mutate: createMutate } = useMutation(createMeetingRecord, {
    onSuccess: (data) => {
      dispatch(
        meetingRecordManagementActions.onEditCreatedMeetingRecordId(data)
      );
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
    },
  });

  const { mutate: deleteMutate } = useMutation(deleteMeetingRecord, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
    },
  });

  const { mutate: editMutate } = useMutation(editMeetingRecord, {
    onSuccess: (data) => {
      dispatch(
        meetingRecordManagementActions.onEditCreatedMeetingRecordId(data)
      );
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
    },
  });

  const { mutate: createBookMarkMutate } = useMutation(
    createBookMarkMeetingRecord,
    {
      onSuccess: () => {
        queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
      },
    }
  );

  return { createMutate, deleteMutate, editMutate, createBookMarkMutate };
};

export default useMeetingRecordManagementMutation;
