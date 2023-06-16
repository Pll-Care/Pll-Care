import { customAxios } from "./customAxios"

export const getAllMeetingRecordList = async (projectId, pageNum=1, direction='DESC') => {
    const response = await customAxios.get(`/auth/memo/list?project_id=${projectId}&page=${pageNum}&size=3&direction=${direction}`);

    return {
        meetingRecordList: response.data.content,
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
    }
}

export const getBookMarkMeetingRecordList = async (projectId, pageNum = 1) =>  {
    const response = await customAxios.get(`/auth/memo/bookmarklist?project_id=${projectId}&page=${pageNum}&size=3`);

    return {
        meetingRecordList: response.data.content,
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages
    }
}

export const createMeetingRecord = async (newMeetingRecordObj) => {
    const response = await customAxios.post(`/auth/memo`, newMeetingRecordObj);

    return response.data.content;
}

export const getMeetingRecord = async (meetingRecordId) => {
    const response = await customAxios.get(`/auth/memo/${meetingRecordId}`);

    return response.data.content;
}