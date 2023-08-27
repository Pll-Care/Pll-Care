import axios from "axios";

export const customAxios = axios.create({
  baseURL: "https://fullcare.store/api",
  headers: {
    "Content-Type": "application/json",
  },
});