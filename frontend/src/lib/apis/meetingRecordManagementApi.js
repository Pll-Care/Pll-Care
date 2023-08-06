import { customAxios } from "./customAxios";

export const getAllMeetingRecordList = async (
  projectId,
  pageNum = 1,
  direction = "DESC"
) => {
  const response = await customAxios.get(
    `/auth/memo/list?project_id=${projectId}&page=${pageNum}&size=3&direction=${direction}`
  );

  return {
    meetingRecordList: response.data.content,
    totalElements: response.data.totalElements,
    totalPages: response.data.totalPages,
  };
};

export const getBookMarkMeetingRecordList = async (projectId, pageNum = 1) => {
  const response = await customAxios.get(
    `/auth/memo/bookmarklist?project_id=${projectId}&page=${pageNum}&size=3`
  );

  return {
    meetingRecordList: response.data.content,
    totalElements: response.data.totalElements,
    totalPages: response.data.totalPages,
  };
};

export const createMeetingRecord = async (newMeetingRecordObj) => {
  const response = await customAxios.post(`/auth/memo`, newMeetingRecordObj);

  return response.data.memoId;
};

export const getMeetingRecord = async (meetingRecordId, projectId) => {
  if (meetingRecordId !== -1) {
    const response = await customAxios.get(
      `/auth/memo/${meetingRecordId}?project_id=${projectId}`
    );

    return response.data;
  }
};

export const deleteMeetingRecord = async (newMeetingRecordObj) => {
  const response = await customAxios.delete(
    `/auth/memo/${newMeetingRecordObj.meetingRecordId}`,
    {
      data: { projectId: newMeetingRecordObj.projectId },
    }
  );

  return response.data;
};

export const createBookMarkMeetingRecord = async (newMeetingRecordObj) => {
  const response = await customAxios.post(
    `/auth/memo/${newMeetingRecordObj.meetingRecordId}/bookmark`,
    {
      projectId: newMeetingRecordObj.projectId,
    }
  );

  return response.data;
};

export const editMeetingRecord = async (newMeetingRecordObj) => {
  const response = await customAxios.put(
    `/auth/memo/${newMeetingRecordObj.selectedMeetingRecordId}`,
    {
      projectId: newMeetingRecordObj.projectId,
      title: newMeetingRecordObj.title,
      content: newMeetingRecordObj.content,
    }
  );

  return response.data.memoId;
};
