/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from "react";
import MainHeader from "../components/common/MainHeader";
import { getToken, isToken } from "../utils/localstroageHandler";
import { useDispatch, useSelector } from "react-redux";
import { authActions } from "../redux/authSlice";
import { useRouter } from "../hooks/useRouter";
import { customAxios } from "../lib/apis/customAxios";

const GeneralLayout = ({ children }) => {
  const dispatch = useDispatch();
  const { currentPath, routeTo } = useRouter();

  const isLogin = useSelector((state) => state.auth.isLoggedIn);

  useEffect(() => {
    const accessToken = getToken("access_token");
    customAxios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${accessToken}`;
  }, [isLogin]);

  useEffect(() => {
    if (currentPath !== "/" && !isToken("access_token")) {
      routeTo("/");
    }

    if (isToken("access_token") && isToken("refresh_token")) {
      dispatch(authActions.login());
    }
  }, []);

  return (
    <>
      <MainHeader />
      <div>{children}</div>
    </>
  );
};

export default GeneralLayout;
