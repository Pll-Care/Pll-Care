import { useState } from "react";
import { useLocation } from "react-router";

import { Avatar } from "@mui/material";
import Tooltip from "@mui/material/Tooltip";

import Button from "../common/Button";
import AlertCheckModal from "../common/Modal/AlertCheckModal";
import ScheduleEvaluationModal from "./ScheduleEvaluationModal";
import ScheduleDetailModal from "./ScheduleDetailModal";
import ScheduleRemainDate from "./ScheduleRemainDate";

import {
  getDateTimeDuration,
  getEnglishWeekdays,
  getStringDate,
} from "../../utils/date";
import { getProjectId } from "../../utils/getProjectId";
import { useCompleteScheduleMutation } from "../../hooks/Mutations/useScheduleManagementMutation";
import { useQuery } from "react-query";
import { getCompleteProjectData } from "../../lib/apis/managementApi";

const ScheduleItem = (props) => {
  const projectId = getProjectId(useLocation());

  // 평가 모달
  const [modalVisible, setModalVisible] = useState(false);
  // 완료 모달
  const [completeModalVisible, setCompleteModalVisible] = useState(false);
  // 디테일 모달
  const [detailModalVisible, setDetailModalVisible] = useState(false);

  const completeBody = {
    scheduleId: props.data.scheduleId,
    projectId: projectId,
    state: "COMPLETE",
  };

  // 완료 처리하는 react query 문
  const { mutate: compeleteSchedule } =
    useCompleteScheduleMutation(completeBody);

  // 중간 평가 모달
  const openModalHandler = () => {
    setModalVisible(true);
  };
  const hideModalHandler = () => {
    setModalVisible(false);
  };

  // 완료 모달
  const openCompleteModalHandler = () => {
    setCompleteModalVisible(true);
  };
  const hideCompleteModalHandler = () => {
    setCompleteModalVisible(false);
  };

  // 디테일 모달
  const openDetailModalHandler = () => {
    setDetailModalVisible((prevState) => !prevState);
  };
  const hideDetailModalHandler = () => {
    setDetailModalVisible((prevState) => !prevState);
  };

  // 완료 확인 react query문
  const { data: isCompleted } = useQuery(
    ["completeProjectData", projectId],
    () => getCompleteProjectData(projectId)
  );

  const time = getDateTimeDuration(
    props.data.startDate,
    props.data.endDate,
    props.data.scheduleCategory
  );
  const modifyDate = getStringDate(new Date(props.data.modifyDate));
  const day = new Date(props.data.startDate).getDate();

  return (
    <div className="schedule-list">
      <ScheduleEvaluationModal
        open={modalVisible}
        id={props.data.scheduleId}
        onClose={hideModalHandler}
        title={props.data.title}
        startDate={props.data.startDate}
        endDate={props.data.endDate}
        members={props.data.members}
        type={props.data.scheduleCategory}
      />

      <AlertCheckModal
        open={completeModalVisible}
        onClose={hideCompleteModalHandler}
        text="예정된 종료 일자보다 먼저 일정이 완료되었습니까?"
        clickHandler={() => compeleteSchedule(completeBody)}
      />

      <ScheduleDetailModal
        open={detailModalVisible}
        onClose={hideDetailModalHandler}
        scheduleId={props.data.scheduleId}
        projectId={projectId}
        scheduleState={props.data.state}
      />

      <div className="schedule-list-time">
        <h1>{day}</h1>
        <h2>{getEnglishWeekdays(props.data.startDate)}</h2>

        {props.option === "pastAll" && (
          <Button text={"일정 완료"} size="small" type={"positive_dark"} />
        )}
        {props.option !== "pastAll" &&
          props.data.state === "COMPLETE" &&
          !isCompleted && (
            <Button
              text={"✍평가 작성"}
              size="small"
              onClick={openModalHandler}
            />
          )}
        {props.data.state === "ONGOING" && !isCompleted && (
          <Button
            text={"🙂완료시키기"}
            size="small"
            onClick={openCompleteModalHandler}
          />
        )}
      </div>

      <div
        className="schedule-list-content"
        onClick={() => openDetailModalHandler()}
      >
        <h5>{time}</h5>
        <div className="schedule-list-content-title">
          <h4>{props.data.title}</h4>
          <ScheduleRemainDate startDate={props.data.startDate} />
        </div>

        <div className="schedule-list-content-option">
          <h5>{modifyDate} 수정</h5>
          <div className="schedule-list-content-option-mui">
            {props.data.members.map((member, index) => (
              <Tooltip key={index} title={member.name}>
                <Avatar src={member.imageUrl} />
              </Tooltip>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
export default ScheduleItem;
