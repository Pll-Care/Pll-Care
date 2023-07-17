import React, { useState, useRef } from "react";
import { useQuery } from "react-query";

import ModalContainer from "../common/ModalContainer";
import { getDetailSchedule } from "../../lib/apis/scheduleManagementApi";
import { getDateTimeDuration } from "../../utils/date";
import Button from "../common/Button";

const ScheduleDetailModal = ({
  open,
  onClose,
  scheduleId,
  projectId,
  scheduleState,
}) => {
  const [formValues, setFormValues] = useState({
    scheduleId: scheduleId,
    projectId: projectId,
    startDate: "",
    endDate: "",
    state: scheduleState,
    memberIds: [],
    title: "",
    content: "",
    category: "",
    address: "",
  });
  const { data, isLoading } = useQuery(
    ["ScheduleDetail", projectId, scheduleId],
    async () => await getDetailSchedule(projectId, scheduleId),
    {
      onSuccess: (data) => {
        setFormValues({
          scheduleId: scheduleId,
          projectId: projectId,
          startDate: data.startDate,
          endDate: data.endDate,
          state: scheduleState,
          memberIds: data.members,
          title: data.title,
          content: data.content,
          category: data.scheduleCategory,
          address: data.scheduleCategory === "MILESTONE" ? "" : data.address,
        });
      },
    }
  );
  const { title, content, category, address, startDate, endDate } = formValues;

  const time = getDateTimeDuration(startDate, endDate, category);

  return (
    <ModalContainer open={open} onClose={onClose}>
      <div className="schedule-detail-modal">
        <h1>{title}</h1>
        <div className="schedule-detail-modal-content">
          <div className="schedule-detail-modal-content-title">
            <h5>진행일시</h5>
            <h5>참여자</h5>
            {category === "MEETING" && <h5>장소</h5>}
            <h5>설명</h5>
          </div>
          <div className="schedule-detail-modal-content-option">
            <h5>{time}</h5>
            {data?.members.map((member) => (
              <Button
                text={member.name}
                key={member.id}
                type={member.in ? "positive_dark" : ""}
                size="small"
              />
            ))}
            {category === "MEETING" && <h5>{address}</h5>}
            <h5>{content}</h5>
          </div>
        </div>
        <div className="schedule-detail-modal-button">
          <Button text="삭제" type="underlined" size="small" />
          <Button text="수정하기" size="small" type="positive" />
        </div>
      </div>
    </ModalContainer>
  );
};
export default ScheduleDetailModal;
