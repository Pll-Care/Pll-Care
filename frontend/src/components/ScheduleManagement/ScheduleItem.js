import { useState } from "react";

import { Avatar } from "@mui/material";
import Tooltip from "@mui/material/Tooltip";

import Button from "../common/Button";
import ScheduleEvaluationModal from "./ScheduleEvaluationModal";
import {
  getDateTimeDuration,
  getEnglishWeekdays,
  getRemainDate,
  getStringDate,
} from "../../utils/date";
import ModifyScheduleModal from "./ModifyScheduleModal";
import AlertModal from "./AlertModal";
import { useParams } from "react-router";

const ScheduleItem = (props) => {
  const [modalVisible, setModalVisible] = useState(false);
  const [modifyModalVisible, setModifyModalVisible] = useState(false);
  const [deleteModalVisible, setDeleteModalVisible] = useState(false);

  const openModalHandler = () => {
    setModalVisible(true);
  };
  const hideModalHandler = () => {
    setModalVisible(false);
  };

  const openModifyModalHandler = () => {
    setModifyModalVisible(true);
  };
  const hideModifyModalHandler = () => {
    setModifyModalVisible(false);
  };

  const openDeleteModalHandler = () => {
    setDeleteModalVisible(true);
  };
  const hideDeleteModalHandler = () => {
    setDeleteModalVisible(false);
  };

  const time = getDateTimeDuration(
    props.data.startDate,
    props.data.endDate,
    props.data.scheduleCategory
  );
  const modifyDate = getStringDate(new Date(props.data.modifyDate));
  const remainDate = getRemainDate(props.data.endDate);
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
      <ModifyScheduleModal
        open={modifyModalVisible}
        onClose={hideModifyModalHandler}
        scheduleId={props.data.scheduleId}
        state={props.data.state}
      />
      <AlertModal
        open={deleteModalVisible}
        onClose={hideDeleteModalHandler}
        width="30%"
        text="정말 일정 삭제하시겠습니까?"
      />
      <div className="schedule-list-time">
        <h1>{day}</h1>
        <h2>{getEnglishWeekdays(props.data.startDate)}</h2>

        {props.data.state !== "TBD" ? (
          <Button
            text={"✍평가 작성"}
            size="small"
            onClick={openModalHandler}
          />
        ) : (
          <Button text={"🙂완료됨"} size="small" />
        )}
      </div>
      <div
        className={`schedule-list-content ${
          remainDate === "past" ? "schedule-list-content-past" : ""
        }`}
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
          <Button
            text="수정하기"
            size="small"
            onClick={openModifyModalHandler}
          />
          <Button
            text="삭제하기"
            size="small"
            onClick={openDeleteModalHandler}
          />
        </div>
      </div>
    </div>
  );
};
export default ScheduleItem;
