import { useState } from "react";
import { useLocation } from "react-router";
import { useQuery } from "react-query";

import CalendarItem from "./CalendarItem";
import ScheduleModal from "./ScheduleModal";
import Button from "../common/Button";
import { Loading } from "../common/Loading";

import { getTodayDateEnglish } from "../../utils/date";
import { getProjectId } from "../../utils/getProjectId";
import { getTodayAfterSchedule } from "../../lib/apis/scheduleManagementApi";

const CalendarList = () => {
  const projectId = getProjectId(useLocation());
  const [modalIsVisible, setModalIsVisible] = useState(false);

  const { data, isLoading, status } = useQuery(["todayAfterSchedule"], () =>
    getTodayAfterSchedule(projectId)
  );

  const modalOpen = () => {
    setModalIsVisible(true);
  };
  const modalClose = () => {
    setModalIsVisible(false);
  };

  // 오늘 날짜 가져오기
  const calendar = getTodayDateEnglish();

  return (
    <div className="calendar-list">
      <div className="calendar-list-time">
        <h5>오늘</h5>
        <h1>{calendar}</h1>
      </div>
      {status === "error" && (
        <h2 className="check-schedule">통신 오류났습니다.</h2>
      )}

      {isLoading && <Loading />}
      {status === "success" && data && data?.length === 0 && (
        <h2 className="check-schedule">오늘 일정이 없습니다.</h2>
      )}

      {status === "success" &&
        data &&
        data?.map((data, index) => <CalendarItem key={index} data={data} />)}

      <div className="button-container">
        <Button text="새 일정 생성" onClick={() => modalOpen()} />
      </div>
      <ScheduleModal open={modalIsVisible} onClose={modalClose} />
    </div>
  );
};
export default CalendarList;
