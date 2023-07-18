import { useState } from "react";

import CalendarItem from "./CalendarItem";
import ScheduleModal from "./ScheduleModal";
import Button from "../common/Button";
import { getTodayDateEnglish } from "../../utils/date";
import {
  getAfterScheduleData,
  getCombineSortedPlanMeeting,
} from "../../utils/schedule";

const CalendarList = ({ data }) => {
  const [modalIsVisible, setModalIsVisible] = useState(false);
  const modalOpen = () => {
    setModalIsVisible(true);
  };
  const modalClose = () => {
    setModalIsVisible(false);
  };

  // 오늘 날짜 가져오기
  const calendar = getTodayDateEnglish();

  // 오늘 이후의 meeting 가져오기
  const filteredMeetings = data?.meetings
    ? getAfterScheduleData(data.meetings)
    : [];

  // 오늘 이후의 plan 가져오기
  const filteredMilestones = data?.milestones
    ? getAfterScheduleData(data.milestones)
    : [];

  // plan과 meeting 시간 순으로 sort해서 합치기
  const sortedEvents = getCombineSortedPlanMeeting(
    filteredMeetings,
    filteredMilestones
  );

  return (
    <div className="calendar-list">
      <div className="calendar-list-time">
        <h5>오늘</h5>
        <h1>{calendar}</h1>
      </div>
      {!sortedEvents && (
        <h1 className="check-schedule">🥲 통신 오류났습니다.</h1>
      )}
      {sortedEvents && sortedEvents.length === 0 && (
        <h1 className="check-schedule">오늘 이후의 일정이 없습니다.</h1>
      )}
      {sortedEvents?.map((data, index) => (
        <CalendarItem key={index} data={data} />
      ))}
      <div className="button-container">
        <Button text="새 일정 생성" onClick={() => modalOpen()} />
      </div>
      <ScheduleModal open={modalIsVisible} onClose={modalClose} />
    </div>
  );
};
export default CalendarList;
