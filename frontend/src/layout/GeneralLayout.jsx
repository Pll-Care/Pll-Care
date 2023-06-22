/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from "react";
import MainHeader from "../components/common/MainHeader";
import { isToken } from "../utils/localstroageHandler";
import { useDispatch } from "react-redux";
import { authActions } from "../redux/authSlice";

const GeneralLayout = ({ children }) => {
  const dispatch = useDispatch();

  useEffect(() => {
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
