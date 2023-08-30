/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable no-unused-vars */
import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { customAxios } from "../../lib/apis/customAxios";

const JWTToken = () => {
  const [searchParams, _] = useSearchParams();

  const accessToken = searchParams.get("access_token");
  const refreshToken = searchParams.get("refresh_token");

  useEffect(() => {
    if (accessToken && refreshToken) {
      localStorage.clear();
      localStorage.setItem("access_token", accessToken);
      localStorage.setItem("refresh_token", refreshToken);

      window.opener?.postMessage("login", "*");

      window.close();

      customAxios.defaults.headers.common["Authorization"] = accessToken
        ? `Bearer ${accessToken}`
        : null;
    }
  }, []);

  return <div></div>;
};

export default JWTToken;
