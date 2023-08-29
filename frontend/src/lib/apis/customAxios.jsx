import axios from "axios";

export const customAxios = axios.create({
  baseURL: "https://fullcare.store/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// customAxios.interceptors.response.use(
//   (response) => {
//     return response;
//   },
//   (error) => {

//   }
// )
