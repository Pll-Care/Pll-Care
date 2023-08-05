/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from "react";
import { useRouter } from "../hooks/useRouter";
import { useDispatch, useSelector } from "react-redux";

import { getToken, isToken } from "../utils/localstroageHandler";
import { authActions } from "../redux/authSlice";
import { customAxios } from "../lib/apis/customAxios";

import Login from "../components/Login/Login";
import MainHeader from "../components/common/MainHeader";

const GeneralLayout = ({ children }) => {
  const dispatch = useDispatch();
  const { currentPath } = useRouter();
  const authState = useSelector((state) => state.auth.isLoggedIn);

  const isLogin = useSelector((state) => state.auth.isLoggedIn);
  const isLoginModalVisible = useSelector(
    (state) => state.auth.isLoginModalVisible
  );

  useEffect(() => {
    const accessToken = getToken("access_token");
    customAxios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${accessToken}`;
  }, [isLogin]);

  useEffect(() => {
    if (isToken("access_token") && isToken("refresh_token")) {
      dispatch(authActions.login());
    }
  }, [currentPath, authState]);

  return (
    <>
      <MainHeader />
      <main>{children}</main>
      {isLoginModalVisible && <Login />}
    </>
  );
};

export default GeneralLayout;
