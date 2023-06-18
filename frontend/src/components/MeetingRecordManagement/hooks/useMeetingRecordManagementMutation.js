import { useMutation, useQueryClient } from "react-query";

import { createBookMarkMeetingRecord, createMeetingRecord, deleteMeetingRecord, editMeetingRecord } from "../../lib/apis/meetingRecordManagementApi";

const useMeetingRecordManagementMutation = () => {
  const queryClient = useQueryClient();

  const { mutate: createMutate } = useMutation(createMeetingRecord, {
    onSuccess: () =>
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]),
  });

  const { mutate: deleteMutate } = useMutation(deleteMeetingRecord, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
    }
  });

  const { mutate: editMutate } = useMutation(editMeetingRecord, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementAllMeetingRecordList"]);
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
    }
  })

  const { mutate: createBookMarkMutate } = useMutation(createBookMarkMeetingRecord, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
    }
  });

  return { createMutate, deleteMutate, editMutate, createBookMarkMutate };
};

export default useMeetingRecordManagementMutation;
