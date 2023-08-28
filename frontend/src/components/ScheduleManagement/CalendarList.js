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
import { getCompleteProjectData } from "../../lib/apis/managementApi";

const CalendarList = () => {
  const projectId = getProjectId(useLocation());
  const [modalIsVisible, setModalIsVisible] = useState(false);

  // 완료 확인 react query문
  const { data: isCompleted } = useQuery(
    ["completeProjectData", projectId],
    () => getCompleteProjectData(projectId)
  );

  // 오늘 일정 조회하는 react query문
  const { data, isLoading, status } = useQuery(
    ["todayAfterSchedule"],
    () => getTodayAfterSchedule(projectId),
    {
      retry: 0,
    }
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
      {isLoading && <Loading />}
      {status === "success" && data && data?.length === 0 && (
        <h5>오늘 회의가 없습니다.</h5>
      )}

      {status === "success" &&
        data &&
        data?.map((data, index) => <CalendarItem key={index} data={data} />)}

      <div className="button-container">
        {!isCompleted && (
          <Button text="새 일정 생성" onClick={() => modalOpen()} />
        )}
      </div>
      <ScheduleModal open={modalIsVisible} onClose={modalClose} />
    </div>
  );
};
export default CalendarList;
