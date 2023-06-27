import { useState } from "react";
import { useQuery } from "react-query";
import { useParams } from "react-router";

import CalendarItem from "./CalendarItem";
import NewScheduleModal from "./NewScheduleModal";
import Button from "../common/Button";
import { getAllSchedule } from "../../lib/apis/scheduleManagementApi";

// 더미 데이터
const datas = {
  meetings: [
    {
      scheduleId: 0,
      title: "string",
      content: "string",
      startDate: "2023-06-25T05:49:53.840Z",
      endDate: "2023-06-25T10:49:53.840Z",
      address: {
        city: "string",
        street: "string",
      },
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
      address: {
        city: "string",
        street: "string",
      },
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
      address: {
        city: "string",
        street: "string",
      },
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
  const today = new Date();
  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  const month = months[today.getMonth()];

  const daysOfWeek = ["Sun", "Mon", "Tues", "Wednes", "Thurs", "Fri", "Satur"];
  const dayOfWeek = daysOfWeek[today.getDay()];
  const date = today.getDate();
  // 오늘 날짜 요일, 일 표시
  const calendar = `${month} ${date}, ${dayOfWeek}`;

  // 모든 일정 가져오기
  const { id } = useParams();

  //const { isLoading, data } = useQuery("CalendarSchedule", () =>
  //  getAllSchedule(id)
  //);
  const filteredMeetings = datas?.meetings?.filter((meeting) => {
    const meetingStartDate = new Date(meeting.startDate);
    return meetingStartDate >= today;
  });

  const filteredMilestones = datas?.milestones?.filter((milestone) => {
    const milestoneStartDate = new Date(milestone.startDate);
    return milestoneStartDate >= today;
  });

  const sortedEvents = [...filteredMeetings, ...filteredMilestones].sort(
    (a, b) => {
      const startDateA = new Date(a.startDate);
      const startDateB = new Date(b.startDate);
      return startDateA - startDateB;
    }
  );
  console.log(sortedEvents);

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
      {modalIsVisible && <NewScheduleModal onClose={modalClose} />}
    </div>
  );
};
export default CalendarList;
