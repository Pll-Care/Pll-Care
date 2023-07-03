import { useQuery } from "react-query";
import { useParams } from "react-router";

import Card from "../common/Card";
import { getFilterSchedule } from "../../lib/apis/scheduleManagementApi";
import {
  getAfterScheduleData,
  getCombineSortedPlanMeeting,
  getPastScheduleData,
} from "../../utils/schedule";
import ScheduleItem from "./ScheduleItem";
import { useMemo } from "react";

const ScheduleList = ({ nameId, option }) => {
  const { id } = useParams();

  const { isLoading, data } = useQuery(
    ["filterSchedule", id, nameId, option],
    () => getFilterSchedule(id, nameId)
  );
  let schedules = useMemo(() => {
    if (data && option === "all") {
      return data;
    }
    if (data && option === "MEETING") {
      return data.filter((item) => item.scheduleCategory === "MEETING");
    }
    if (data && option === "MILESTONE") {
      return data.filter((item) => item.scheduleCategory === "MILESTONE");
    }
    if (data && option === "pastAll") {
      return getPastScheduleData(data);
    }
  }, [data, option]);

  console.log(option, "에 따른", schedules);
  return (
    <Card className="schedule-lists">
      {isLoading && <h1 className="check-schedule-gray">⏳ 로딩 중...</h1>}
      {!isLoading && schedules && schedules.length === 0 && (
        <h1 className="check-schedule-gray">해당 일정이 없습니다</h1>
      )}
      {!schedules && !isLoading && (
        <h1 className="check-schedule-gray">🥲 통신 오류났습니다.</h1>
      )}
      {schedules &&
        !isLoading &&
        schedules.length > 0 &&
        schedules.map((schedule, index) => (
          <ScheduleItem key={index} data={schedule} />
        ))}
    </Card>
  );
};
export default ScheduleList;
