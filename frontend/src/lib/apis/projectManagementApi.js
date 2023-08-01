import { customAxios } from "./customAxios";

export const getProjectList = async (pageNum = 1, state) => {
  let response = null;

  if (state === "ALL") {
    response = await customAxios.get(
      `/auth/project?page=${pageNum}&size=4&state=ONGOING&state=COMPLETE`
    );
  } else {
    response = await customAxios.get(
      `/auth/project?page=${pageNum}&size=4&state=${state}`
    );
  }

  return {
    projectList: response.data.content,
    totalElements: response.data.totalElements,
    totalPages: response.data.totalPages,
  };
};

export const createProject = async (newProjectObj) => {
  const response = await customAxios.post("/auth/project", newProjectObj);

  return response.data.content;
};

export const deleteProject = async (projectId) => {
  const response = await customAxios.delete(`/auth/project/${projectId}`);

  return response.data.content;
};

export const editProject = async (newProjectData) => {
  const response = await customAxios.put(
    `/auth/project/${newProjectData.projectId}`,
    {
      title: newProjectData.title,
      description: newProjectData.description,
      state: newProjectData.state,
      startDate: newProjectData.startDate,
      endDate: newProjectData.endDate,
      imageUrl: newProjectData.imageUrl,
    }
  );

  return response.data;
};

export const completeProject = async (projectId) => {
  const response = await customAxios.put(`/auth/project/${projectId}/state`, {
    state: "COMPLETE",
  });

  return response.data;
};

export const uploadImage = async (imgData) => {
  const response = await customAxios.post(
    `/auth/upload/image?dir=${imgData.dir}`,
    {
      file: imgData.formData,
    },
    {
      headers: { "Content-Type": "multipart/form-data" },
    }
  );

  return response.data.imageUrl;
};

export const getProjectData = async (projectId) => {
  const response = await customAxios.get(`/auth/project/${projectId}`);

  return response.data;
};
