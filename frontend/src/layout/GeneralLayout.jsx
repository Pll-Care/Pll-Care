/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from "react";
import MainHeader from "../components/common/MainHeader";
import { getToken, isToken } from "../utils/localstroageHandler";
import { useDispatch, useSelector } from "react-redux";
import { authActions } from "../redux/authSlice";
import { useRouter } from "../hooks/useRouter";
import { customAxios } from "../lib/apis/customAxios";
import Login from "../components/Login/Login";

const GeneralLayout = ({ children }) => {
  const dispatch = useDispatch();
  const { currentPath } = useRouter();

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
      dispatch(authActions.setIsLoginModalVisible(false));
    }
  }, [currentPath]);

  useEffect(() => {
    console.log(isLoginModalVisible);
  }, [isLoginModalVisible])

  return (
    <>
      <MainHeader />
      <main>{children}</main>
      {isLoginModalVisible && <Login />}
    </>
  );
};

export default GeneralLayout;
