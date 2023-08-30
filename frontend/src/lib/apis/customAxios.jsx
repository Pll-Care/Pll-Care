import axios from "axios";

export const customAxios = axios.create({
  baseURL: "https://fullcare.store/api",
  headers: {
    "Content-Type": "application/json",
  },
});

const accessToken = localStorage.getItem("access_token");

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
