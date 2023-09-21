import { useEffect } from "react";
import { useQuery } from "react-query";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router";
import { toast } from "react-toastify";
import {
  VerticalTimeline,
  VerticalTimelineElement,
} from "react-vertical-timeline-component";
import "react-vertical-timeline-component/style.min.css";

import ComputerIcon from "@mui/icons-material/Computer";
import EmojiEventsIcon from "@mui/icons-material/EmojiEvents";
import FlagIcon from "@mui/icons-material/Flag";

import { getOverviewAllSchedule } from "../../lib/apis/scheduleManagementApi";
import { authActions } from "../../redux/authSlice";
import { getDateTimeDuration } from "../../utils/date";
import { getProjectId } from "../../utils/getProjectId";
import { isToken } from "../../utils/localstorageHandler";
import Card from "../common/Card";

const OverviewChart = () => {
  const projectId = getProjectId(useLocation());
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const { isLoading, data, status } = useQuery(
    "overviewSchedule",
    () => getOverviewAllSchedule(projectId),
    {
      retry: 0,
      onError: (error) => {
        if (error.response.data.code === "PROJECT_004") {
          navigate("/management");
          toast.error("해당 오버뷰 페이지 접근 권한이 없습니다.");
        }
      },
    }
  );

  useEffect(() => {
    if (!isToken("access_token")) {
      navigate("/");
      dispatch(authActions.setIsLoginModalVisible(true));
    }
  }, [dispatch, navigate]);

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

  // 최소 order
  const minOrder = data
    ? Math.min(...data?.schedules.map((schedule) => schedule.order))
    : null;
  // 최대 order
  const maxOrder = data
    ? Math.max(...data?.schedules.map((schedule) => schedule.order))
    : null;

  const month = data && new Date(data.startDate).getMonth() - 1 + minOrder;

  // order 값에 해당하는 배열 생성
  const orderGroups = [];
  if (data) {
    for (let i = minOrder; i <= maxOrder; i++) {
      const group = data?.schedules.filter((schedule) => schedule.order === i);
      orderGroups.push(group.length > 0 ? group : []);
    }
  }

  return (
    <div className="overview-container">
      <Card className="overview-wrapper">
        <div className="schedule">
          <h1>주요 일정 미리보기</h1>
          {status === "error" && (
            <h2 className="check-schedule-gray">통신 오류났습니다.</h2>
          )}
          {status === "success" && (
            <VerticalTimeline lineColor="#01E89E">
              {/*프로젝트 시작*/}
              {data && !isLoading && (
                <VerticalTimelineElement
                  className="vertical-timeline-element--work"
                  date={data.startDate}
                  iconStyle={{ background: "#01E89E", color: "white" }}
                  icon={<EmojiEventsIcon />}
                >
                  <h3 className="endpoint">🙌 프로젝트 start</h3>
                  {data.dateCategory === "MONTH" && (
                    <h5 className="category">달별로 일정 미리보기</h5>
                  )}
                  {data.dateCategory === "WEEK" && (
                    <h5 className="category">주별로 일정 미리보기</h5>
                  )}
                </VerticalTimelineElement>
              )}

              {/*계획이 없는 경우*/}
              {data && !isLoading && data?.schedules.length === 0 && (
                <VerticalTimelineElement
                  className="vertical-timeline-element-small"
                  contentStyle={{
                    background: "#01E89E",
                    fontFamily: "Noto Sans KR",
                    borderRadius: "15px",
                  }}
                  contentArrowStyle={{ borderRight: "7px solid  #01E89E" }}
                  iconStyle={{
                    background: "#01E89E",
                    color: "white",
                  }}
                  icon={<ComputerIcon />}
                >
                  <h5 className="plan" style={{ color: "white" }}>
                    아직 계획이 없습니다.
                  </h5>
                </VerticalTimelineElement>
              )}

              {/*프로젝트 일정들*/}
              {data &&
                !isLoading &&
                orderGroups &&
                orderGroups?.map((schedule, index) => (
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
                    {data?.dateCategory === "WEEK" && (
                      <h2 className="overview-time">
                        {(index + 1) * 2 - 1}주 ~ {(index + 1) * 2}주차
                      </h2>
                    )}
                    {data?.dateCategory === "MONTH" && (
                      <h2 className="overview-time">
                        {months[(month + index) % 12]}
                      </h2>
                    )}
                    {schedule?.length === 0 && (
                      <div className="simple">
                        <h2>해당 일정이 없습니다</h2>
                      </div>
                    )}

                    {schedule?.length > 0 &&
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
                  date={data.endDate}
                  iconStyle={{ background: "#01E89E", color: "white" }}
                  icon={<FlagIcon />}
                >
                  <h3 className="endpoint">👏 프로젝트 finish</h3>
                </VerticalTimelineElement>
              )}
            </VerticalTimeline>
          )}
        </div>
      </Card>
    </div>
  );
};
export default OverviewChart;
