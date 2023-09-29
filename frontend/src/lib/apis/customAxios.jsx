import axios from "axios";

export const customAxios = axios.create({
  baseURL: "https://pll-care.store/api",
  headers: {
    "Content-Type": "application/json",
  },
});

const accessToken = localStorage.getItem("access_token");

customAxios.defaults.headers.common["Authorization"] = accessToken
  ? `Bearer ${accessToken}`
  : null;
