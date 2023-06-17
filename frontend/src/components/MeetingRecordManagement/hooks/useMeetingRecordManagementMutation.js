import { useMutation, useQueryClient } from "react-query";

import { createMeetingRecord } from "../../lib/apis/meetingRecordManagementApi";

const useMeetingRecordManagementMutation = () => {
  const queryClient = useQueryClient();

  const { mutate: createMutate } = useMutation(createMeetingRecord, {
    onSuccess: () =>
      queryClient.invalidateQueries(["managementMeetingRecordList"]),
  });

  return { createMutate };
};

export default useMeetingRecordManagementMutation;
