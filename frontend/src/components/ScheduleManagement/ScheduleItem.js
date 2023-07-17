import { useState } from "react";
import { useLocation } from "react-router";

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
import {
  useCompleteScheduleMutation,
  useDeleteScheduleMutation,
} from "../../lib/apis/scheduleManagementApi";
import ScheduleModal from "./ScheduleModal";
import { getProjectId } from "../../utils/getProjectId";
import { Link } from "react-router-dom";
import ScheduleDetailModal from "./ScheduleDetailModal";

const ScheduleItem = (props) => {
  const projectId = getProjectId(useLocation());

  const [modalVisible, setModalVisible] = useState(false);
  const [modifyModalVisible, setModifyModalVisible] = useState(false);
  const [deleteModalVisible, setDeleteModalVisible] = useState(false);
  const [completeModalVisible, setCompleteModalVisible] = useState(false);
  const [detailModalVisible, setDetailModalVisible] = useState(false);

  const deleteBody = {
    scheduleId: props.data.scheduleId,
    projectId: projectId,
  };
  const completeBody = {
    scheduleId: props.data.scheduleId,
    projectId: projectId,
    state: "COMPLETE",
  };
  // 삭제 처리하는 react query 문
  const { mutate: deleteSchedule } = useDeleteScheduleMutation(deleteBody);

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

  // 수정 모달
  const openModifyModalHandler = () => {
    setModifyModalVisible(true);
  };
  const hideModifyModalHandler = () => {
    setModifyModalVisible(false);
  };

  // 삭제 모달
  const openDeleteModalHandler = () => {
    setDeleteModalVisible(true);
  };
  const hideDeleteModalHandler = () => {
    setDeleteModalVisible(false);
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

          {/*<Button
            text="수정하기"
            size="small"
            onClick={openModifyModalHandler}
          />

          <Button
            text="삭제하기"
            size="small"
            onClick={openDeleteModalHandler}
          />*/}
        </div>
      </div>
    </div>
  );
};
export default ScheduleItem;
