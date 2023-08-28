import { useLocation, useNavigate } from "react-router";
import { useQuery } from "react-query";
import { toast } from "react-toastify";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";

import CalendarList from "./CalendarList";
import {
  getCalendarAllSchedule,
  getOverviewAllSchedule,
} from "../../lib/apis/scheduleManagementApi";
import { getProjectId } from "../../utils/getProjectId";

const MyCalendar = () => {
  const projectId = getProjectId(useLocation());
  const navigate = useNavigate();

  const { data, status } = useQuery(
    ["calendarSchedule", projectId],
    () => getCalendarAllSchedule(projectId),
    {
      retry: 0,
      onError: (error) => {
        if (error.response.data.code === "PROJECT_004") {
          navigate("/management");
          toast.error("해당 일정 접근 권한이 없습니다.");
        }
      },
    }
  );

  const { data: overview } = useQuery("overviewSchedule", () =>
    getOverviewAllSchedule(projectId)
  );

  // 달력에 표시할 모든 일정들을 저장할 배열
  const events = [];

  const start = {
    title: "👏start",
    date: overview.startDate,
    color: "#bebebe",
  };
  events.push(start);

  const finish = {
    title: "🏆finish",
    date: overview.endDate,
    color: "#bebebe",
  };
  events.push(finish);

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
      html: `<div>${arg.event.title}</div>`,
    };
  };

  // 달력 간격 커스텀
  const dayCellContent = (args) => {
    return (
      <div style={{ width: "20px", height: "10px" }}>{args.dayNumberText}</div>
    );
  };

  return (
    <div className="schedule-calendar">
      <div className="schedule-calendar-schedulelist">
        {status === "success" && (
          <FullCalendar
            defaultView="dayGridMonth"
            plugins={[dayGridPlugin]}
            eventContent={eventContent}
            events={events}
            dayCellContent={dayCellContent}
          />
        )}
        {status === "error" && (
          <FullCalendar
            defaultView="dayGridMonth"
            plugins={[dayGridPlugin]}
            eventContent={eventContent}
            dayCellContent={dayCellContent}
          />
        )}
      </div>
      <CalendarList />
    </div>
  );
};
export default MyCalendar;
