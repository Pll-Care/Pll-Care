import { useState } from "react";
import { useQuery } from "react-query";
import { useParams } from "react-router";

import CalendarItem from "./CalendarItem";
import NewScheduleModal from "./NewScheduleModal";
import Button from "../common/Button";
import { getAllSchedule } from "../../lib/apis/scheduleManagementApi";
import { getTodayDateEnglish } from "../../utils/date";
import {
  getAfterScheduleData,
  getCombineSortedPlanMeeting,
} from "../../utils/schedule";

// 더미 데이터
const datas = {
  meetings: [
    {
      scheduleId: 0,
      title: "string",
      content: "string",
      startDate: "2023-06-25T05:49:53.840Z",
      endDate: "2023-06-25T10:49:53.840Z",
      address: "string1",
      members: [
        {
          id: 0,
          name: "string",
          imageUrl: "string",
        },
      ],
    },
    {
      scheduleId: 1,
      title: "string1",
      content: "string1",
      startDate: "2023-06-29T05:49:53.840Z",
      endDate: "2023-06-29T10:49:53.840Z",
      address: "string2",
      members: [
        {
          id: 0,
          name: "string",
          imageUrl: "string",
        },
      ],
    },
    {
      scheduleId: 1,
      title: "string3",
      content: "string1",
      startDate: "2023-07-26T05:49:53.840Z",
      endDate: "2023-07-26T09:49:53.840Z",
      address: "string3",
      members: [
        {
          id: 0,
          name: "string",
          imageUrl: "string",
        },
      ],
    },
  ],
  milestones: [
    {
      scheduleId: 0,
      title: "string",
      content: "string",
      startDate: "2023-05-26T05:49:53.840Z",
      endDate: "2023-05-29T05:49:53.840Z",
      members: [
        {
          id: 0,
          name: "string",
          imageUrl: "string",
        },
      ],
    },
    {
      scheduleId: 0,
      title: "string",
      content: "string",
      startDate: "2023-07-26T05:49:53.840Z",
      endDate: "2023-07-29T05:49:53.840Z",
      members: [
        {
          id: 0,
          name: "string",
          imageUrl: "string",
        },
      ],
    },
  ],
};

const CalendarList = () => {
  const [modalIsVisible, setModalIsVisible] = useState(false);
  const modalOpen = () => {
    setModalIsVisible(true);
  };
  const modalClose = () => {
    setModalIsVisible(false);
  };

  // 오늘 날짜 가져오기
  const calendar = getTodayDateEnglish();

  // 모든 일정 가져오기
  const { id } = useParams();

  //const { isLoading, data } = useQuery("CalendarSchedule", () =>
  //  getAllSchedule(id)
  //);

  // 오늘 이후의 meeting 가져오기
  const filteredMeetings = getAfterScheduleData(datas.meetings);

  // 오늘 이후의 plan 가져오기
  const filteredMilestones = getAfterScheduleData(datas.milestones);

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
      {sortedEvents.map((data, index) => (
        <CalendarItem key={index} data={data} />
      ))}
      <div className="button-container">
        <Button text="새 일정 생성" onClick={() => modalOpen()} />
      </div>
      <NewScheduleModal open={modalIsVisible} onClose={modalClose} />
    </div>
  );
};
export default CalendarList;
