import { Fragment, useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";

import { authActions } from "../../redux/authSlice";
import { isToken } from "../../utils/localstorageHandler";
import MyCalendar from "./MyCalendar";
import ScheduleOption from "./ScheduleOption";

const ScheduleManagementPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isToken("access_token")) {
      navigate("/");
      dispatch(authActions.setIsLoginModalVisible(true));
    }
  }, [dispatch, navigate]);

  return (
    <Fragment>
      <MyCalendar />
      <ScheduleOption />
    </Fragment>
  );
};
export default ScheduleManagementPage;
