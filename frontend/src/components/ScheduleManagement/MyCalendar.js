import { useParams } from "react-router";
import { useQuery } from "react-query";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";

import CalendarList from "./CalendarList";
import { getAllSchedule } from "../../lib/apis/scheduleManagementApi";

const MyCalendar = () => {
  const { id } = useParams();

  const { isLoading, data } = useQuery("CalendarSchedule", () =>
    getAllSchedule(id)
  );
  console.log(data);

  // 더미 데이터
  const datas = {
    meetings: [
      {
        scheduleId: 0,
        title: "string",
        content: "string",
        startDate: "2023-06-26T05:49:53.840Z",
        endDate: "2023-06-26T05:49:53.840Z",
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
        endDate: "2023-06-29T05:49:53.840Z",
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
        endDate: "2023-07-26T05:49:53.840Z",
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

  // 달력에 표시할 모든 일정들을 저장할 배열
  const events = [];
  datas.meetings.forEach((meetings) => {
    const meeting = {
      title: meetings.title,
      date: meetings.startDate.slice(0, 10),
      color: "#00aa72",
    };
    events.push(meeting);
  });

  datas.milestones.forEach((milestones) => {
    const milestone = {
      title: milestones.title,
      start: milestones.startDate.slice(0, 10),
      end: milestones.endDate.slice(0, 10),
      color: "#01e89e",
    };
    events.push(milestone);
  });
  //console.log(events);

  // 일정 표시하는 부분 커스텀
  const eventContent = (arg) => {
    return {
      html: `<div>💻${arg.event.title}</div>`,
    };
  };

  // 달력 간격 커스텀
  const dayCellContent = (args) => {
    return (
      <div style={{ width: "80%", height: "80%", margin: "auto" }}>
        {args.dayNumberText}
      </div>
    );
  };

  return (
    <div className="calendar">
      <div className="calendar-schedule">
        <FullCalendar
          defaultView="dayGridMonth"
          plugins={[dayGridPlugin]}
          eventContent={eventContent}
          events={events}
          dayCellContent={dayCellContent}
        />
      </div>
      <CalendarList />
    </div>
  );
};
export default MyCalendar;
