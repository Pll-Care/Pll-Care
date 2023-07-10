import axios from "axios";

export const getAllRecruitmentPost = async (page) => {
  try {
    const res = await axios.get(
      `/auth/post/list?page=1&size=1&direction=ASC&sortingProperty=string`
    );
    return res;
  } catch (err) {
    return err;
  }
};
