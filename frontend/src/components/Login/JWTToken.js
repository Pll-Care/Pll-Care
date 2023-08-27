/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable no-unused-vars */
import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";

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
    }
  }, []);

  return <div></div>;
};

export default JWTToken;
