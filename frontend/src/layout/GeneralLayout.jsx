/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

import { authActions } from "../redux/authSlice";

import Login from "../components/Login/Login";
import MainHeader from "../components/common/Header/MainHeader";
import LocalTokenRepository from "../lib/repository/LocalTokenRepository";
import GeneralClientProvider from "../context/Client/GeneralClientContext";
import GeneralService from "../lib/service/GeneralService";
import HttpClient from "../lib/service/HttpClient";
import { baseURL } from "../utils/auth";
import { getToken } from "../utils/localstorageHandler";
import { customAxios } from "../lib/apis/customAxios";

const GeneralLayout = ({ children }) => {
  const dispatch = useDispatch();
  const localTokenRepository = new LocalTokenRepository();
  const httpClient = new HttpClient(baseURL, localTokenRepository);
  const generalService = new GeneralService(httpClient);

  const isLoginModalVisible = useSelector(
    (state) => state.auth.isLoginModalVisible
  );

  useEffect(() => {
    window.addEventListener("message", (event) => {
      if (event.data === "login") {
        dispatch(authActions.login());
      }
    });

    const accessToken = getToken("access_token");
    customAxios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${accessToken}`;
  }, []);

  useEffect(() => {
    if (
      !!localTokenRepository.get("access") &&
      !!localTokenRepository.get("refresh")
    )
      dispatch(authActions.login());
  }, []);

  return (
    <GeneralClientProvider generalService={generalService}>
      <MainHeader />
      <main>{children}</main>
      {isLoginModalVisible && <Login />}
    </GeneralClientProvider>
  );
};

export default GeneralLayout;
