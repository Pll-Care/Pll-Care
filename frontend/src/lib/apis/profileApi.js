import { customAxios } from "./customAxios";

export const searchTechAPI = async (tech) => {
  try {
    const response = await customAxios.get(`/auth/util/techstack?tech=${tech}`);
    if (response) return response.data;
  } catch (error) {
    console.log(error);
  }
};
