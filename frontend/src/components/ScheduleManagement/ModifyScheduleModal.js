import { useState, useRef } from "react";
import { useParams } from "react-router";
import { toast } from "react-toastify";
import { useMutation, useQuery } from "react-query";

import Button from "../common/Button";
import {
  ModifySchedule,
  getDetailSchedule,
} from "../../lib/apis/scheduleManagementApi";
import ModalContainer from "../common/ModalContainer";

// 프로젝트 팀원 더미 데이터

const ModifyScheduleModal = ({
  open,
  onClose,
  scheduleId,
  state,
  scheduleInfo,
}) => {
  const { id } = useParams();
  const today = new Date();
  const [startDate, setStartDate] = useState();
  const [endDate, setEndDate] = useState();
  const [participants, setParticipants] = useState([]);
  const [title, setTitle] = useState();
  const [content, setContent] = useState();
  const [option, setOption] = useState("MILESTONE");
  const [location, setLocation] = useState("");
  //console.log("모달에서 ", scheduleInfo);

  const { data } = useQuery(
    "ScheduleDetail",
    () => getDetailSchedule(id, scheduleId),
    {
      onSuccess: (data) => {
        setStartDate(data.startDate);
        setEndDate(data.endDate);
        setParticipants(data.members);
        setTitle(data.title);
        setContent(data.content);
        setOption(data.scheduleCategory);
        //if (data.address === null) {
        //  setLocation("");
        //} else {
        setLocation(data.address);
        //}
        setLocation(data.address);
      },
    }
  );
  //console.log(data);

  const titleRef = useRef();
  const contentRef = useRef();
  const startDateRef = useRef();
  const endDateRef = useRef();
  const locationRef = useRef();

  const handleChangeStartDate = (e) => {
    setStartDate(e.target.value);
  };

  const handleChangeEndDate = (e) => {
    setEndDate(e.target.value);
  };

  const handleChangeTitle = (e) => {
    setTitle(e.target.value);
  };

  const handleChangeContent = (e) => {
    setContent(e.target.value);
  };

  const handleChangeOption = (e) => {
    setOption(e.target.value);
  };

  const handleChangeLocation = (e) => {
    setLocation(e.target.value);
  };

  const cancelModal = () => {
    onClose();
  };

  const handleButtonClick = (data) => {
    setParticipants((prevParticipants) => {
      const updatedParticipants = prevParticipants.map((participant) => {
        if (participant.id === data.id) {
          return { ...participant, in: !participant.in };
        }
        return participant;
      });
      return updatedParticipants;
    });
  };
  //const { mutate } = useMutation("ModifySchedule", ModifySchedule, {
  //  onSuccess: (data) => {
  //    console.log(data);
  //    onClose();
  //  },
  //});

  // 수정하는 버튼
  const submitModifyPlan = () => {
    const members = [];
    participants.forEach((participant) => {
      if (participant.in === true) {
        members.push(participant.id);
      }
    });
    // 수정 내용 검증
    if (
      title === data.title &&
      content === data.content &&
      startDate === data.startDate &&
      endDate === data.endDate &&
      participants === data.members &&
      option === "MEETING" &&
      location === data.address
    ) {
      toast.error("수정하지 않으셨습니다. 일정 수정해주세요");
      return;
    }
    if (title.length < 2) {
      toast.error("일정 제목을 더 작성해주세요.");
      titleRef.current.focus();
      return;
    }
    const start = new Date(startDate);
    const end = new Date(endDate);
    if (start >= end) {
      toast.error("일정 기간을 수정해주세요.");
      endDateRef.current.focus();
      return;
    }
    if (option === "MEETING" && start.toDateString() !== end.toDateString()) {
      toast.error("모임의 일정은 같은 일로만 가능합니다.");
      startDateRef.current.focus();
      endDateRef.current.focus();
      return;
    }
    if (members.length < 1) {
      toast.error("일정 참여할 멤버를 선택해주세요.");
      return;
    }
    if (content.length < 2) {
      toast.error("일정 내용 설명을 더 작성해주세요.");
      contentRef.current.focus();
      return;
    }
    const modifyData = {
      projectId: parseInt(id, 10),
      startDate: startDate,
      endDate: endDate,
      state: state,
      category: option,
      memberIds: members,
      title: title,
      content: content,
      address: location,
    };

    console.log(modifyData);
  };

  return (
    <ModalContainer open={open} onClose={onClose} type="light" width="50%">
      <div className="modal-container">
        <input
          type="text"
          ref={titleRef}
          required
          placeholder="일정 제목을 입력해주세요"
          value={title}
          onChange={handleChangeTitle}
        />
        <div className="modal-container-description">
          <div className="plan-option">
            <h5>카테고리</h5>
            <select onChange={handleChangeOption}>
              <option value="MILESTONE">새 계획 생성</option>
              <option value="MEETING">새 회의 생성</option>
            </select>
          </div>
          <div className="plan-option">
            <h5>진행 기간</h5>
            <input
              type="datetime-local"
              ref={startDateRef}
              required
              value={startDate}
              onChange={handleChangeStartDate}
              data-placeholder="시작 일자"
            />
            <h4>~</h4>
            <input
              type="datetime-local"
              ref={endDateRef}
              required
              value={endDate}
              onChange={handleChangeEndDate}
              data-placeholder="종료 일자"
            />
          </div>
          {option === "MEETING" && (
            <div className="plan-option-bottom">
              <h5>모임 위치</h5>
              <input
                type="text"
                ref={locationRef}
                required
                placeholder="장소를 입력하세요"
                value={location}
                onChange={handleChangeLocation}
              />
            </div>
          )}

          <div className="plan-option">
            <h5>참여자</h5>
            {participants.map((data) => (
              <Button
                key={data.id}
                text={data.name}
                size="small"
                type={
                  participants.find((participant) => participant.in === true)
                    ? "positive_dark"
                    : ""
                }
                onClick={() => handleButtonClick(data)}
              />
            ))}
          </div>
        </div>
        <div className="modal-container-description">
          <div className="plan-option">
            <h5>내용 입력</h5>
            <input
              type="text"
              ref={contentRef}
              required
              placeholder="내용을 입력하세요"
              value={content}
              onChange={handleChangeContent}
            />
          </div>
        </div>
        <div className="button-container">
          <Button text="수정 완료" onClick={submitModifyPlan} />
          <Button text="취소" onClick={cancelModal} />
        </div>
      </div>
    </ModalContainer>
  );
};
export default ModifyScheduleModal;
