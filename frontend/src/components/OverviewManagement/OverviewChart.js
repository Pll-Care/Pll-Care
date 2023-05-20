import {
  VerticalTimeline,
  VerticalTimelineElement,
} from "react-vertical-timeline-component";
import "react-vertical-timeline-component/style.min.css";
import EmojiEventsIcon from "@mui/icons-material/EmojiEvents";
import FlagIcon from "@mui/icons-material/Flag";
import ComputerIcon from "@mui/icons-material/Computer";
import Card from "../Card";

const datas = [
  {
    id: 1,
    date: "Apr, 15",
    meetings: [
      {
        id: 1,
        title: "간단한 일정 제목1-1",
        description: "필요한 정보(시간, 참여자 등)",
      },
      {
        id: 2,
        title: "간단한 일정 제목1-2",
        description: "필요한 정보(시간, 참여자 등)",
      },
    ],
  },
  {
    id: 2,
    date: "May, 1",
    meetings: [
      {
        id: 1,
        title: "간단한 일정 제목2-1",
        description: "필요한 정보(시간, 참여자 등)",
      },
      {
        id: 2,
        title: "간단한 일정 제목2-2",
        description: "필요한 정보(시간, 참여자 등)",
      },
      {
        id: 3,
        title: "간단한 일정 제목2-3",
        description: "필요한 정보(시간, 참여자 등)",
      },
    ],
  },
];

const OverviewChart = () => {
  return (
    <Card className="schedule">
      <h1>주요 일정 미리보기</h1>
      <VerticalTimeline lineColor="#01E89E">
        <VerticalTimelineElement
          className="vertical-timeline-element-small"
          contentStyle={{
            color: "#01E89E",
            fontFamily: "IBM Plex Sans KR",
            fontWeight: "700",
            fontSize: "32px",
          }}
          date="Start"
          iconStyle={{
            background: "#01E89E",
            color: "white",
          }}
          icon={<EmojiEventsIcon />}
        />

        {datas.map((data) => (
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
              {data.date}
            </h2>
            {data.meetings.map((meeting) => (
              <div className="simple">
                <h2>{meeting.title}</h2>
                <h3>{meeting.description}</h3>
              </div>
            ))}
          </VerticalTimelineElement>
        ))}

        <VerticalTimelineElement
          className="vertical-timeline-element-small"
          contentStyle={{
            color: "#01E89E",
            fontFamily: "IBM Plex Sans KR",
            fontWeight: "700",
            fontSize: "32px",
          }}
          date="Finish"
          iconStyle={{
            background: "#01E89E",
            color: "white",
          }}
          icon={<FlagIcon />}
        />
      </VerticalTimeline>
    </Card>
  );
};
export default OverviewChart;
