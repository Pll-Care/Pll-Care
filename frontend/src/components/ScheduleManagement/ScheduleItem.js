import { useState } from "react";

import { useLocation } from "react-router";

import { Avatar } from "@mui/material";
import Tooltip from "@mui/material/Tooltip";

import Button from "../common/Button";
import ScheduleEvaluationModal from "./ScheduleEvaluationModal";
import AlertModal from "./AlertModal";
import ScheduleDetailModal from "./ScheduleDetailModal";

import {
  getDateTimeDuration,
  getEnglishWeekdays,
  getRemainDate,
  getStringDate,
} from "../../utils/date";
import { getProjectId } from "../../utils/getProjectId";
import { useCompleteScheduleMutation } from "../../hooks/useScheduleManagementMutation";

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

  const time = getDateTimeDuration(
    props.data.startDate,
    props.data.endDate,
    props.data.scheduleCategory
  );
  const modifyDate = getStringDate(new Date(props.data.modifyDate));
  const remainDate = getRemainDate(props.data.startDate);
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

      <AlertModal
        open={completeModalVisible}
        onClose={hideCompleteModalHandler}
        text="일정이 완료되었습니까?"
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

        {props.data.state === "COMPLETE" && (
          <Button
            text={"✍평가 작성"}
            size="small"
            onClick={openModalHandler}
          />
        )}
        {props.data.state === "ONGOING" && (
          <Button
            text={"🙂완료시키기"}
            size="small"
            onClick={openCompleteModalHandler}
          />
        )}
      </div>

      <div
        className={`schedule-list-content ${
          remainDate === "past" ? "schedule-list-content-past" : ""
        }`}
        onClick={() => openDetailModalHandler()}
      >
        <div>
          <h5>{time}</h5>
          <h4>{props.data.title}</h4>
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

        <div className="schedule-list-content-time">
          {remainDate !== "past" && <h1>{remainDate}</h1>}
        </div>
      </div>
    </div>
  );
};
export default ScheduleItem;
