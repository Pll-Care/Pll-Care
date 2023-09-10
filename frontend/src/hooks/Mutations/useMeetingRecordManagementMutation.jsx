import { useMutation, useQueryClient } from "react-query";

import { useDispatch } from "react-redux";
import { meetingRecordManagementActions } from "../../redux/meetingRecordManagementSlice";
import { useManagementClient } from "../../context/Client/ManagementClientContext";

const useMeetingRecordManagementMutation = () => {
  const queryClient = useQueryClient();

  const dispatch = useDispatch();

  const {
    createMeetingRecord,
    deleteMeetingRecord,
    editMeetingRecord,
    createBookMarkMeetingRecord,
  } = useManagementClient();

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
