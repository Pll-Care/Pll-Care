import { Avatar } from "@mui/material";
import Tooltip from "@mui/material/Tooltip";

import Button from "../Button";
import { useState } from "react";
import ScheduleEvaluationModal from "./ScheduleEvalutaionModal";
import { useSelector } from "react-redux";

const Schedule = (props) => {
  const evaluations = useSelector((state) => state.evaluationManagement);
  const findEvaluation = evaluations.evaluationManagement.find(
    (e) => e.id === props.data.id
  );
  console.log(findEvaluation);

  const [modalVisible, setModalVisible] = useState(false);

  const openModalHandler = () => {
    setModalVisible(true);
  };
  const hideModalHandler = () => {
    setModalVisible(false);
  };

  const completeClickHandler = () => {};
  const members = ["김철수", "박영수", "최민수", "김영희", "김민지"];

  return (
    <div className="schedule-list">
      {modalVisible && (
        <ScheduleEvaluationModal
          id={props.data.id}
          modalHandler={hideModalHandler}
          title={props.data.title}
          time={props.data.time}
          day={props.data.day}
          week={props.data.week}
        />
      )}
      <div className="schedule-list-time">
        <h1>{props.data.day}</h1>
        <h2>{props.data.week}</h2>

        {!findEvaluation && (
          <Button
            text={"평가 작성"}
            onClick={openModalHandler}
            type="positive"
          />
        )}
        {findEvaluation && (
          <Button
            text={"완료하기"}
            onClick={completeClickHandler}
            type="positive"
          />
        )}
        {findEvaluation && findEvaluation.complete === "완료됨" && (
          <Button text={"완료됨"} onClick={openModalHandler} type="positive" />
        )}
      </div>

      <div className="schedule-list-content">
        <h5>{props.data.time}</h5>
        <div className="schedule-list-content-time">
          <h4>{props.data.title}</h4>
          <h1>D-day</h1>
        </div>

        <div className="schedule-list-content-option">
          <h5>2023.04.23 수정</h5>
          {members.map((member, index) => (
            <Tooltip key={index} title={member}>
              <Avatar />
            </Tooltip>
          ))}
        </div>
      </div>
    </div>
  );
};
export default Schedule;
