import FullCalendar from "@fullcalendar/react";
import CalendarList from "./CalendarList";
import dayGridPlugin from "@fullcalendar/daygrid";
import { customAxios } from "../lib/apis/customAxios";
import { useParams } from "react-router";
import { useQuery } from "react-query";

const MyCalendar = () => {
  const { id } = useParams();

  // 월에 있는 일정 가져오는 api 함수
  const getMonthSchedule = async () => {
    try {
      const res = await customAxios.get(
        `/auth/schedule/calenderList?project_id=${id}&year=2023&month=6`
      );
      return res.data;
    } catch (err) {
      return err;
    }
  };

  //const { isLoading, data, refetch } = useQuery(
  //  "CalendarSchedule",
  //  getMonthSchedule
  //);
  //console.log(data);

  const events = [
    {
      title: "plan1",
      start: "2023-06-01",
      end: "2023-06-08",
      color: "#01e89e",
    },
    {
      title: "meeting1",
      date: "2023-06-25",
      color: "#00aa72",
    },
  ];

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
