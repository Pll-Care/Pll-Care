import { useState, useEffect, useCallback } from "react";
import { useParams } from "react-router";
import { useQuery } from "react-query";

import Card from "../common/Card";
import Button from "../common/Button";
import { Loading } from "../common/Loading";
import ScheduleList from "./ScheduleList";
import { useScheduleClient } from "../../context/Client/ScheduleClientContext";

const ScheduleOption = () => {
  const { id } = useParams();

  const { getTeamMember } = useScheduleClient();
  const { data: names, isLoading } = useQuery(["members", id], () =>
    getTeamMember(id)
  );
  const options = [
    { name: "ALL", type: "all" },
    { name: "PLAN", type: "MILESTONE" },
    { name: "MEETING", type: "MEETING" },
    { name: "PREVIOUS", type: "pastAll" },
  ];

  const [optionVisible, setOptionVisible] = useState(options[0].type);
  const [nameVisible, setNameVisible] = useState();

  const optionClickHandler = useCallback(
    (option) => {
      setOptionVisible(option);
    },
    [setOptionVisible]
  );

  const nameClickHandler = useCallback(
    (id) => {
      setNameVisible(id);
    },
    [setNameVisible]
  );

  useEffect(() => {
    if (!isLoading && names && names.length > 0) {
      setNameVisible(names[0].memberId);
    }
  }, [isLoading, names]);

  return (
    <div className="schedule-page-container">
      <div className="schedule-option-container">
        <div className="schedule-option">
          {options.map((option, index) => (
            <Button
              key={index}
              radius="big"
              text={option.name}
              color={optionVisible === option.type ? "white" : "green"}
              type={optionVisible === option.type ? "positive_radius" : ""}
              onClick={() => optionClickHandler(option.type)}
            />
          ))}
        </div>
        <Card className="schedule-option-person">
          <h4>참여자</h4>
          {isLoading && <Loading />}
          {!names && !isLoading && (
            <h1 className="check-schedule">통신 오류났습니다.</h1>
          )}
          {!isLoading &&
            names?.map((data, index) => (
              <Button
                key={index}
                text={data.name}
                type={nameVisible === data.memberId ? "positive_dark" : ""}
                onClick={() => nameClickHandler(data.memberId)}
              />
            ))}
        </Card>
        {nameVisible && (
          <ScheduleList option={optionVisible} nameId={nameVisible} />
        )}
      </div>
    </div>
  );
};
export default ScheduleOption;
