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

customAxios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response.data.code === "GLOBAL_001") {
      toast.error("OAUTH2 소셜 로그인에 실패했습니다.");
    }
    if (error.response.data.code === "GLOBAL_002") {
      toast.error("API 요청시 전달되는 데이터가 올바르지 않습니다.");
    }
    if (error.response.data.code === "GLOBAL_003") {
      toast.error("권한이 없습니다.");
    }
    if (error.response.data.code === "GLOBAL_004") {
      toast.error("내부 서버 에러입니다.");
    }
    if (error.response.data.code === "JWT_001") {
      toast.error("잘못된 JWT 서명입니다.");
    }
    if (error.response.data.code === "JWT_002") {
      toast.error("만료된 JWT 토큰입니다.");
    }
    if (error.response.data.code === "JWT_003") {
      toast.error("지원되지 않는 JWT 토큰 형식입니다.");
    }
    if (error.response.data.code === "JWT_004") {
      toast.error("비정상적인 JWT 토큰입니다.");
    }
    if (error.response.data.code === "JWT_005") {
      toast.error("등록되지 않은 사용자입니다.");
    }
    if (error.response.data.code === "JWT_006") {
      toast.error("리프레시 토큰도 만료되었으니 다시 로그인해주세요.");
    }
    if (error.response.data.code === "JWT_007") {
      toast.error("요청에 토큰이 없어 인증에 실패했습니다.");
    }
  }
)
