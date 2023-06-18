import { useMutation, useQueryClient } from "react-query";

import { createBookMarkMeetingRecord, createMeetingRecord, deleteMeetingRecord } from "../../lib/apis/meetingRecordManagementApi";

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

  const { mutate: createBookMarkMutate } = useMutation(createBookMarkMeetingRecord, {
    onSuccess: () => {
      queryClient.invalidateQueries(["managementBookMarkMeetingRecordList"]);
    }
  })

  return { createMutate, deleteMutate, createBookMarkMutate };
};

export default useMeetingRecordManagementMutation;
