import { Fragment } from "react";
import MyCalendar from "./MyCalendar";
import ScheduleOption from "./ScheduleOption";

const ScheduleManagementPage = () => {
  return (
    <Fragment>
      <MyCalendar />
      <ScheduleOption />
    </Fragment>
  );
};
export default ScheduleManagementPage;
