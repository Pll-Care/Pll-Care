import React, { useRef, useState } from "react";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { useLocation } from "react-router-dom";
import { toast } from "react-toastify";

import CloseIcon from "@mui/icons-material/Close";
import { useMediaQuery } from "@mui/material";

import Button from "../common/Button";
import ModalContainer from "../common/Modal/ModalContainer";

import { getProjectId } from "../../utils/getProjectId";
import { query } from "../../utils/mediaQuery";
import { useScheduleClient } from "../../context/Client/ScheduleClientContext";

const ScheduleModal = ({
  open,
  onClose,
  editScheduleId = null,
  scheduleState = "TBD",
}) => {
  const projectId = getProjectId(useLocation());
  const isMobile = useMediaQuery(query);

  const { createNewSchedule, getTeamMember } = useScheduleClient();
  // 멤버 리스트 받아오기
  const { data: names } = useQuery(["members", projectId], () =>
    getTeamMember(projectId)
  );

  const today = new Date();
  const initialDate = today.toISOString().slice(0, 16);

  const [formValues, setFormValues] = useState({
    scheduleId: editScheduleId,
    projectId: projectId,
    startDate: initialDate,
    endDate: initialDate,
    state: scheduleState,
    memberIds: [],
    title: "",
    content: "",
    category: "MILESTONE",
    address: "",
  });

  // 일정 생성하는 react query 문
  const queryClient = useQueryClient();
  const { mutate: addSchedule, isLoading: addIsLoading } = useMutation(
    createNewSchedule,
    {
      onSuccess: () => {
        queryClient.invalidateQueries("calendarSchedule");
        queryClient.invalidateQueries("filterSchedule");
        queryClient.invalidateQueries("overviewSchedule");
        queryClient.invalidateQueries("todayAfterSchedule");
        onClose();
        setFormValues({
          projectId: projectId,
          scheduleId: editScheduleId,
          startDate: initialDate,
          endDate: initialDate,
          state: scheduleState,
          memberIds: [],
          title: "",
          content: "",
          category: "MILESTONE",
          location: "",
        });

        toast.success("일정이 생성되었습니다");
      },

      onError: (error) => {
        if (error.response.data.code === "SCHEDULE_006") {
          toast.error("해당 일정이 프로젝트 기간에 벗어났습니다");
        } else if (error.response.data.code === "GLOBAL_003") {
          toast.error("해당 일정의 기간이 현재 또는 미래 날짜여야 합니다");
        } else {
          toast.error("서버 에러가 발생했습니다. 잠시후에 시도해주세요");
        }
      },
    }
  );

  const { startDate, endDate, memberIds, title, content, category, address } =
    formValues;

  const inputRefs = {
    title: useRef(),
    content: useRef(),
    startDate: useRef(),
    endDate: useRef(),
    address: useRef(),
    location: useRef(),
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // 사용자 이름 버튼 클릭했을 때
  const handleButtonClick = (data) => {
    setFormValues((prevState) => {
      const updatedParticipants = prevState.memberIds.includes(data.memberId)
        ? prevState.memberIds.filter((member) => member !== data.memberId)
        : [...prevState.memberIds, data.memberId];

      return {
        ...prevState,
        memberIds: updatedParticipants,
      };
    });
  };

  // 일정 생성
  const submitNewPlan = () => {
    if (title.length < 2) {
      toast.error("일정 제목을 작성해주세요.");
      inputRefs.title.current.focus();
      return;
    }
    const start = new Date(startDate);
    const end = new Date(endDate);
    if (category === "MEETING" && start.toDateString() !== end.toDateString()) {
      toast.error("모임의 일정은 같은 일로만 가능합니다.");
      inputRefs.startDate.current.focus();
      inputRefs.endDate.current.focus();
      return;
    }
    if (category === "MEETING" && address.length < 2) {
      toast.error("모임 위치를 더 자세히 작성해주세요.");
      inputRefs.address.current.focus();
      return;
    }
    if (memberIds.length < 1) {
      toast.error("일정 참여할 멤버를 선택해주세요.");
      return;
    }
    if (content.length < 2) {
      toast.error("일정 내용 설명을 더 작성해주세요.");
      inputRefs.content.current.focus();
      return;
    }
    // 일정 생성하기
    const { state, scheduleId, ...formData } = formValues;
    addSchedule(formData);
  };

  return (
    <ModalContainer
      open={open}
      onClose={onClose}
      type="light"
      width={isMobile ? "100%" : "700px"}
      height={isMobile && "100%"}
      border={isMobile && "0px"}
    >
      <div className="modal-container">
        <div className="modal-container-title">
          <input
            className="modal-container-title-input"
            type="text"
            ref={inputRefs.title}
            required
            placeholder="일정 제목을 입력해주세요"
            name="title"
            value={title}
            onChange={handleChange}
          />
          {isMobile && <CloseIcon onClick={onClose} />}
        </div>

        <div className="modal-container-description">
          <div className="plan-option">
            <h5>카테고리</h5>
            <select name="category" value={category} onChange={handleChange}>
              <option value="MILESTONE">새 계획 생성</option>
              <option value="MEETING">새 회의 생성</option>
            </select>
          </div>
          <div className="plan-option">
            <h5>진행 기간</h5>
            <div className="plan-option-duration">
              <input
                type="datetime-local"
                ref={inputRefs.startDate}
                required
                name="startDate"
                value={startDate}
                onChange={handleChange}
                data-placeholder="시작 일자"
              />
              <h4>~</h4>
              <input
                type="datetime-local"
                ref={inputRefs.endDate}
                required
                name="endDate"
                value={endDate}
                onChange={handleChange}
                data-placeholder="종료 일자"
              />
            </div>
          </div>
          {category === "MEETING" && (
            <div className="plan-option-bottom">
              <h5>모임 위치</h5>
              <input
                type="text"
                ref={inputRefs.address}
                required
                placeholder="장소를 입력하세요"
                name="address"
                value={address}
                onChange={handleChange}
              />
            </div>
          )}

          <div className="plan-option">
            <h5>참여자</h5>
            <div className="plan-option-members">
              {names?.map((data) => (
                <Button
                  key={data.memberId}
                  text={data.name}
                  color={memberIds.includes(data.memberId) ? "white" : "green"}
                  size="button_small"
                  type={memberIds.includes(data.memberId) ? "positive" : ""}
                  onClick={() => handleButtonClick(data)}
                />
              ))}{" "}
            </div>
          </div>
        </div>
        <div className="modal-container-content">
          {isMobile ? (
            <div className="plan-option-mobile">
              <h5>내용 입력</h5>
              <textarea
                type="text"
                ref={inputRefs.content}
                required
                placeholder="내용을 입력하세요"
                name="content"
                value={content}
                onChange={handleChange}
              />
            </div>
          ) : (
            <div className="plan-option">
              <h5>내용 입력</h5>
              <input
                type="text"
                ref={inputRefs.content}
                required
                placeholder="내용을 입력하세요"
                name="content"
                value={content}
                onChange={handleChange}
              />
            </div>
          )}
        </div>
        <div className="button-container">
          {isMobile && <Button text="취소" onClick={onClose} />}
          {!addIsLoading && (
            <Button
              text="생성 완료"
              color="white"
              type="positive"
              size="button_small"
              onClick={submitNewPlan}
            />
          )}
          {addIsLoading && (
            <Button
              text="로딩 중.."
              color="white"
              type="positive"
              size="button_small"
            />
          )}
        </div>
      </div>
    </ModalContainer>
  );
};

export default ScheduleModal;
