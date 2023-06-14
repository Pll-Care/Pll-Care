import { customAxios } from "./customAxios";

export const getProjectList = async (pageNum = 1, size = 4, state = 'all') => {
    let response = null;

    if (state === 'all') {
        response = await customAxios.get(`/auth/project?page=${pageNum}&size=${size}&state=ONGOING&state=COMPLETE`);
    } else {
        response = await customAxios.get(`/auth/project?page=${pageNum}&size=${size}&state=${state}`);
    }
    
    return {
        projectList: response.data.content,
        totalElements: response.data.totalElements,
        totalPages: response.data.totalPages,
    }
}

export const createProject = async (newProjectObj) => {
    const response = await customAxios.post('/auth/project', newProjectObj);

    return response.data.content;
}

export const deleteProject = async (projectId) => {
    const response = await customAxios.delete(`/auth/project/${projectId}`);

    return response.data.content;
}