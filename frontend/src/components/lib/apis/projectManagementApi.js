import { customAxios } from "./customAxios";

export const getProjectList = async (pageNum = 1, state = 'all') => {
    let response = null;

    if (state === 'all') {
        response = await customAxios.get(`/auth/project?page=${pageNum}&size=4&state=ONGOING&state=COMPLETE`);
    } else {
        response = await customAxios.get(`/auth/project?page=${pageNum}&size=4&state=${state}`);
    }
    
    return {
        projectList: response.data.contents,
        totalElements: response.data.paging.totalElements,
        totalPages: response.data.paging.totalPages,
    }
}

export const createProject = async (newProjectObj) => {
    const response = await customAxios.post('/auth/project', newProjectObj);

    return response.data.contents;
}

export const deleteProject = async (projectId) => {
    const response = await customAxios.delete(`/auth/project/${projectId}`);

    return response.data.contents;
}