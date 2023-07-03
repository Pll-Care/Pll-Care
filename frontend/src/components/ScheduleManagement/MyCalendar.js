import { useParams } from "react-router";
import { useQuery } from "react-query";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";

import CalendarList from "./CalendarList";
import { getCalendarAllSchedule } from "../../lib/apis/scheduleManagementApi";

const MyCalendar = () => {
  const { id } = useParams();

  const { data } = useQuery(
    ["calendarSchedule", id],
    () => getCalendarAllSchedule(id),
    {
      onSuccess: (data) => {
        console.log("calendarSchedule", "이 샐행됨", data);
      },
    }
  );
  //console.log("calendar 데이터", data);

  // 달력에 표시할 모든 일정들을 저장할 배열
  const events = [];
  data?.meetings?.forEach((meetings) => {
    const meeting = {
      title: meetings.title,
      date: meetings.startDate.slice(0, 10),
      color: "#00aa72",
    };
    events.push(meeting);
  });

  data?.milestones?.forEach((milestones) => {
    const milestone = {
      title: milestones.title,
      start: milestones.startDate.slice(0, 10),
      end: milestones.endDate.slice(0, 10),
      color: "#01e89e",
    };
    events.push(milestone);
  });

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
      <CalendarList data={data} />
    </div>
  );
};
export default MyCalendar;
