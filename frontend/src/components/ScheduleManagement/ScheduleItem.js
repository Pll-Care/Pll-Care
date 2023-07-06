import { useState } from "react";
import { useLocation } from "react-router";
import { useSelector } from "react-redux";

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
import AlertModal from "./AlertModal";
import { useDeleteScheduleMutation } from "../../lib/apis/scheduleManagementApi";
import ScheduleModal from "./ScheduleModal";
import { getProjectId } from "../../utils/getProjectId";
import { isCompleteProject } from "../../utils/isCompleteProject";

const ScheduleItem = (props) => {
  const projectId = getProjectId(useLocation());
  // 완료 처리했을 때
  const completedProjectId = useSelector(
    (state) => state.projectManagement.completedProjectId
  );
  const isComplete = isCompleteProject(completedProjectId, projectId);

  const [modalVisible, setModalVisible] = useState(false);
  const [modifyModalVisible, setModifyModalVisible] = useState(false);
  const [deleteModalVisible, setDeleteModalVisible] = useState(false);
  const deleteBody = {
    scheduleId: props.data.scheduleId,
    projectId: parseInt(projectId, 10),
  };
  const { mutate: deleteSchedule } = useDeleteScheduleMutation(deleteBody);

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
      <ScheduleModal
        open={modifyModalVisible}
        onClose={hideModifyModalHandler}
        isEdit={true}
        editScheduleId={props.data.scheduleId}
        scheduleState={props.data.state}
      />

      <AlertModal
        open={deleteModalVisible}
        onClose={hideDeleteModalHandler}
        width="30%"
        text="정말 일정 삭제하시겠습니까?"
        clickHandler={() => deleteSchedule(deleteBody)}
      />
      <div className="schedule-list-time">
        <h1>{day}</h1>
        <h2>{getEnglishWeekdays(props.data.startDate)}</h2>

        {props.data.state === "ONGOING" && isComplete === "ONGOING" && (
          <Button
            text={"✍평가 작성"}
            size="small"
            onClick={openModalHandler}
          />
        )}
        {props.data.state === "COMPLETE" && isComplete === "ONGOING" && (
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
          {isComplete === "ONGOING" && (
            <Button
              text="수정하기"
              size="small"
              onClick={openModifyModalHandler}
            />
          )}
          {isComplete === "ONGOING" && (
            <Button
              text="삭제하기"
              size="small"
              onClick={openDeleteModalHandler}
            />
          )}
        </div>
      </div>
    </div>
  );
};
export default ScheduleItem;
