import { useParams } from "react-router";
import { useQuery } from "react-query";
import {
  VerticalTimeline,
  VerticalTimelineElement,
} from "react-vertical-timeline-component";
import "react-vertical-timeline-component/style.min.css";

import EmojiEventsIcon from "@mui/icons-material/EmojiEvents";
import FlagIcon from "@mui/icons-material/Flag";
import ComputerIcon from "@mui/icons-material/Computer";

import Card from "../common/Card";
import { getOverviewAllSchedule } from "../../lib/apis/scheduleManagementApi";

// 더미 데이터
const datas = {
  startDate: "2023-06-17",
  endDate: "2023-09-17",
  dateCategory: "MONTH",
  schedules: [
    [
      {
        scheduleId: 0,
        title: "string",
        startDate: "2023-06-17T03:20:13.310Z",
        endDate: "2023-06-19T03:20:13.310Z",
        order: 0,
      },
      {
        scheduleId: 0,
        title: "string",
        startDate: "2023-06-19T03:20:13.310Z",
        endDate: "2023-06-27T03:20:13.310Z",
        order: 0,
      },
    ],
    [
      {
        scheduleId: 0,
        title: "string",
        startDate: "2023-07-17T03:20:13.310Z",
        endDate: "2023-07-17T03:20:13.310Z",
        order: 0,
      },
      {
        scheduleId: 0,
        title: "string",
        startDate: "2023-08-17T03:20:13.310Z",
        endDate: "2023-08-17T03:20:13.310Z",
        order: 0,
      },
    ],
    [
      {
        scheduleId: 0,
        title: "string",
        startDate: "2023-07-17T03:20:13.310Z",
        endDate: "2023-07-17T03:20:13.310Z",
        order: 0,
      },
      {
        scheduleId: 0,
        title: "string",
        startDate: "2023-08-17T03:20:13.310Z",
        endDate: "2023-08-17T03:20:13.310Z",
        order: 0,
      },
    ],
  ],
};

const OverviewChart = () => {
  const { id } = useParams();

  const { isLoading, data, refetch } = useQuery("overviewSchedule", () =>
    getOverviewAllSchedule(id)
  );

  return (
    <Card>
      <div className="schedule">
        <h1>주요 일정 미리보기</h1>
        <VerticalTimeline lineColor="#01E89E">
          {/*프로젝트 시작*/}
          <VerticalTimelineElement
            className="vertical-timeline-element--work"
            contentStyle={{
              color: "#01E89E",
              fontFamily: "IBM Plex Sans KR",
              fontWeight: "700",
              fontSize: "32px",
            }}
            date={datas.startDate}
            iconStyle={{ background: "#01E89E", color: "white" }}
            icon={<EmojiEventsIcon />}
          >
            <h3 className="endpoint">🙌 프로젝트 start</h3>
            {datas.dateCategory === "MONTH" && (
              <h5 className="endpoint">달별로 일정 미리보기</h5>
            )}
            {datas.dateCategory !== "MONTH" && (
              <h5 className="endpoint">주별로 일정 미리보기</h5>
            )}
          </VerticalTimelineElement>

          {/*프로젝트 일정들*/}
          {datas.schedules.map((data, index) => (
            <VerticalTimelineElement
              key={index}
              className="vertical-timeline-element-small"
              contentStyle={{
                background: "#01E89E",
                color: "white",
                borderRadius: "15px",
              }}
              contentArrowStyle={{ borderRight: "7px solid  #01E89E" }}
              iconStyle={{
                background: "#01E89E",
                color: "white",
              }}
              icon={<ComputerIcon />}
            >
              <h2
                className="vertical-timeline-element-title"
                style={{ color: "white", fontWeight: "bold", fontSize: "20px" }}
              >
                JUNE
              </h2>
              {data.map((meeting) => (
                <div className="simple">
                  <h2>{meeting.title}</h2>
                  <h3>{meeting.startDate}</h3>
                  <h3>{meeting.endDate}</h3>
                </div>
              ))}
            </VerticalTimelineElement>
          ))}

          {/*프로젝트 종료*/}
          <VerticalTimelineElement
            className="vertical-timeline-element--work"
            contentStyle={{
              color: "#01E89E",
              fontFamily: "IBM Plex Sans KR",
              fontWeight: "700",
              fontSize: "32px",
            }}
            date={datas.endDate}
            iconStyle={{ background: "#01E89E", color: "white" }}
            icon={<FlagIcon />}
          >
            <h3 className="endpoint">👏 프로젝트 finish</h3>
          </VerticalTimelineElement>
        </VerticalTimeline>
      </div>
    </Card>
  );
};
export default OverviewChart;
