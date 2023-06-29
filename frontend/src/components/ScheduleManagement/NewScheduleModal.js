import { useState, useRef } from "react";
import { useParams } from "react-router";
import { toast } from "react-toastify";
import { useMutation } from "react-query";

import { Box, Modal } from "@mui/material";

import Button from "../common/Button";
import { makeNewPlan } from "../../lib/apis/scheduleManagementApi";
import ModalContainer from "../common/ModalContainer";

// 프로젝트 팀원 더미 데이터
const names = [
  {
    id: 0,
    pmId: 0,
    name: "string1",
    imageUrl: "string1",
    position: "string1",
  },
  {
    id: 1,
    pmId: 1,
    name: "string2",
    imageUrl: "string2",
    position: "string2",
  },
  {
    id: 2,
    pmId: 2,
    name: "string3",
    imageUrl: "string3",
    position: "string3",
  },
  {
    id: 3,
    pmId: 3,
    name: "string4",
    imageUrl: "string4",
    position: "string4",
  },
];

const NewScheduleModal = ({ open, onClose }) => {
  const { id } = useParams();
  const today = new Date();
  const initialDate = today.toISOString().slice(0, 16);
  const [startDate, setStartDate] = useState(initialDate);
  const [endDate, setEndDate] = useState(initialDate);
  const [participants, setParticipants] = useState([]);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [option, setOption] = useState("MILESTONE");
  const [location, setLocation] = useState("");

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

  const handleButtonClick = (data) => {
    if (participants.find((participant) => participant.id === data.id)) {
      // 클릭한 data가 이미 participants 배열에 있을 경우 제외
      setParticipants(
        participants.filter((participant) => participant.id !== data.id)
      );
    } else {
      // 클릭한 data가 participants 배열에 없을 경우 추가
      setParticipants([
        ...participants,
        { id: data.id, name: data.name, imageUrl: data.imageUrl },
      ]);
    }
  };

  const { mutate } = useMutation(makeNewPlan, {
    onSuccess: (data) => {
      console.log(data);
    },
    onError: (error) => {
      console.log(error);
    },
  });

  const submitNewPlan = () => {
    if (title.length < 2) {
      toast.error("일정 제목을 더 작성해주세요.");
      titleRef.current.focus();
      return;
    }
    const start = new Date(startDate);
    const end = new Date(endDate);
    if (start <= today) {
      toast.error("일정의 시작 일이 오늘 이전일 수 없습니다.");
      startDateRef.current.focus();
      return;
    }
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
    if (option === "MEETING" && location.length < 2) {
      toast.error("모임 위치를 더 자세히 작성해주세요.");
      return;
    }
    if (participants.length < 1) {
      toast.error("일정 참여할 멤버를 선택해주세요.");
      return;
    }
    if (content.length < 2) {
      toast.error("일정 내용 설명을 더 작성해주세요.");
      contentRef.current.focus();
      return;
    }

    const new_plan = {
      projectId: parseInt(id, 10),
      startDate: startDate,
      endDate: endDate,
      category: option,
      memberDtos: participants,
      title: title,
      content: content,
      address: location,
    };
    console.log(new_plan);
    //mutate(new_plan);
  };

  return (
    <ModalContainer open={open} onClose={onClose} type="white" width="60%">
      <div className="modal-container">
        <input
          type="text"
          ref={titleRef}
          required
          placeholder="New Plan Title"
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
            {names.map((data) => (
              <Button
                key={data.id}
                text={data.name}
                size="small"
                type={
                  participants.find((participant) => participant.id === data.id)
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
          <Button text="생성 완료" onClick={submitNewPlan} />
        </div>
      </div>
    </ModalContainer>
  );
};
export default NewScheduleModal;
