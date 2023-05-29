import { useState } from "react";

import Card from "../Card";
import ScheduleList from "./ScheduleList";
import ButtonList from "./ButtonList";

const ScheduleOption = () => {
  const options = ["전체", "Plan", "Meeting", "지난 일정"];
  const participants = ["김철수", "박영수", "최민수", "김영희", "김민지"];

  const [optionVisible, setOptionVisible] = useState();
  const [nameVisible, setNameVisible] = useState();

  const optionClickHandler = (option) => {
    setOptionVisible(option);
  };

  const participantsClickHandler = (name) => {
    setNameVisible(name);
  };
  return (
    <>
      <div className="schedule-option">
        <ButtonList names={options} onButtonClick={optionClickHandler} />
      </div>
      <Card className="schedule-option-person">
        <h4>참여자</h4>
        <ButtonList
          names={participants}
          onButtonClick={participantsClickHandler}
        />
      </Card>

      <ScheduleList option={optionVisible} name={nameVisible} />
    </>
  );
};
export default ScheduleOption;
