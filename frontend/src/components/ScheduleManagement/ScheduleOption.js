import { useState } from "react";

import Card from "../common/Card";
import ScheduleList from "./ScheduleList";
import Button from "../common/Button";

// 프로젝트 팀원 더미 데이터
const names = [
  {
    id: 1,
    name: "string1",
    imageUrl: "string1",
    position: "string1",
  },
  {
    id: 2,
    name: "string2",
    imageUrl: "string2",
    position: "string2",
  },
  {
    id: 3,
    name: "string3",
    imageUrl: "string3",
    position: "string3",
  },
  {
    id: 4,
    name: "string4",
    imageUrl: "string4",
    position: "string4",
  },
];
const ScheduleOption = () => {
  const options = ["ALL", "Plan", "Meeting", "지난 Plan", "지난 Meeting"];

  const [optionVisible, setOptionVisible] = useState(options[0]);
  const [nameVisible, setNameVisible] = useState(names[0].id);

  const optionClickHandler = (option) => {
    setOptionVisible(option);
  };

  const nameClickHandler = (id) => {
    setNameVisible(id);
  };

  return (
    <>
      <div className="schedule-option">
        {options.map((option, index) => (
          <Button
            key={index}
            text={option}
            type={optionVisible === option ? "positive_dark" : ""}
            onClick={() => optionClickHandler(option)}
          />
        ))}
      </div>
      <Card className="schedule-option-person">
        <h4>참여자</h4>
        {names.map((data, index) => (
          <Button
            key={index}
            text={data.name}
            type={nameVisible === data.id ? "positive_dark" : ""}
            onClick={() => nameClickHandler(data.id)}
          />
        ))}
      </Card>
      <ScheduleList option={optionVisible} nameId={nameVisible} />
    </>
  );
};
export default ScheduleOption;
