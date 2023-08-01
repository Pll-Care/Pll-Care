import { customAxios } from "./customAxios";

export const getProfileImage = async () => {
  try {
    const response = await customAxios.get("/auth/member/image");
    if (response.status === 200) return response.data;
  } catch (error) {
    console.log(error);
  }
};
