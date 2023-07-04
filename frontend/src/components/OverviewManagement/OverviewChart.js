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
import { getDateTimeDuration } from "../../utils/date";

const OverviewChart = () => {
  const { id } = useParams();

  const { isLoading, data } = useQuery("overviewSchedule", () =>
    getOverviewAllSchedule(id)
  );
  console.log(data);

  const month = data && new Date(data.startDate).getMonth();

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
  console.log(months[month]);

  // 최소 order
  const minOrder = data
    ? Math.min(...data?.schedules.map((schedule) => schedule.order))
    : null;
  // 최대 order
  const maxOrder = data
    ? Math.max(...data?.schedules.map((schedule) => schedule.order))
    : null;

  console.log(minOrder, maxOrder);

  // order 값에 해당하는 배열 생성
  const orderGroups = [];
  if (data) {
    for (let i = minOrder; i <= maxOrder; i++) {
      const group = data?.schedules.filter((schedule) => schedule.order === i);
      orderGroups.push(group.length > 0 ? group : []);
    }
  }

  return (
    <Card>
      <div className="schedule">
        <h1>주요 일정 미리보기</h1>
        <VerticalTimeline lineColor="#01E89E">
          {/*프로젝트 시작*/}
          {data && !isLoading && (
            <VerticalTimelineElement
              className="vertical-timeline-element--work"
              contentStyle={{
                color: "#01E89E",
                fontFamily: "IBM Plex Sans KR",
                fontWeight: "700",
                fontSize: "32px",
              }}
              date={data.startDate}
              iconStyle={{ background: "#01E89E", color: "white" }}
              icon={<EmojiEventsIcon />}
            >
              <h3 className="endpoint">🙌 프로젝트 start</h3>
              {data.dateCategory === "MONTH" && (
                <h5 className="endpoint">달별로 일정 미리보기</h5>
              )}
              {data.dateCategory === "WEEK" && (
                <h5 className="endpoint">주별로 일정 미리보기</h5>
              )}
            </VerticalTimelineElement>
          )}

          {/*계획이 없는 경우*/}
          {data && !isLoading && data.schedules.length === 0 && (
            <VerticalTimelineElement
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
                아직 계획이 없습니다
              </h2>
            </VerticalTimelineElement>
          )}

          {/*프로젝트 일정들*/}
          {data &&
            !isLoading &&
            orderGroups.map((schedule, index) => (
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
                {data.dateCategory === "WEEK" && (
                  <h2
                    className="vertical-timeline-element-title"
                    style={{
                      color: "white",
                      fontWeight: "bold",
                      fontSize: "20px",
                    }}
                  >
                    {index + 1} 주차
                  </h2>
                )}
                {data.dateCategory === "MONTH" && (
                  <h2
                    className="vertical-timeline-element-title"
                    style={{
                      color: "white",
                      fontWeight: "bold",
                      fontSize: "20px",
                    }}
                  >
                    {months[(month + index) % 12]}
                  </h2>
                )}
                {schedule.length === 0 && (
                  <div className="simple">
                    <h2>해당 일정이 없습니다</h2>
                  </div>
                )}

                {schedule.length > 0 &&
                  schedule?.map((meeting, meetingIndex) => (
                    <div key={meetingIndex} className="simple">
                      <h2>{meeting.title}</h2>
                      <h3>
                        {getDateTimeDuration(
                          meeting.startDate,
                          meeting.endDate,
                          "MILESTONE"
                        )}
                      </h3>
                    </div>
                  ))}
              </VerticalTimelineElement>
            ))}

          {/*프로젝트 종료*/}
          {data && !isLoading && (
            <VerticalTimelineElement
              className="vertical-timeline-element--work"
              contentStyle={{
                color: "#01E89E",
                fontFamily: "IBM Plex Sans KR",
                fontWeight: "700",
                fontSize: "32px",
              }}
              date={data.endDate}
              iconStyle={{ background: "#01E89E", color: "white" }}
              icon={<FlagIcon />}
            >
              <h3 className="endpoint">👏 프로젝트 finish</h3>
            </VerticalTimelineElement>
          )}
        </VerticalTimeline>
      </div>
    </Card>
  );
};
export default OverviewChart;
