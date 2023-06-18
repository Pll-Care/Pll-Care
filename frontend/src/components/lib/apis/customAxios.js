import axios from "axios";

const accessToken = localStorage.getItem("access_token");

export const customAxios = axios.create({
  baseURL:
    "http://ec2-54-180-193-41.ap-northeast-2.compute.amazonaws.com:8080/api",
  headers: {
    Authorization: `Bearer ${accessToken}`,
    "Content-Type": "application/json",
  },
});
