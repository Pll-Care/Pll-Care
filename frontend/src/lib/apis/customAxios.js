import axios from "axios";

import { toast } from "react-toastify";

const accessToken = localStorage.getItem("access_token");

export const customAxios = axios.create({
  baseURL: "https://fullcare.store/api",
  headers: {
    "Content-Type": "application/json",
  },
});

customAxios.defaults.headers.common["Authorization"] = accessToken
  ? `Bearer ${accessToken}`
  : null;

// customAxios.interceptors.response.use(
//   (response) => {
//     return response;
//   },
//   (error) => {

//   }
// )
