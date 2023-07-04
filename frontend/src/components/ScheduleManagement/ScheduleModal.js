import React, { useState, useRef } from "react";
import { useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { useQuery } from "react-query";

import Button from "../common/Button";
import ModalContainer from "../common/ModalContainer";
import { getTeamMember } from "../../lib/apis/teamMemberManagementApi";
import {
  getDetailSchedule,
  useAddNewScheduleMutation,
  useModifyScheduleMutation,
} from "../../lib/apis/scheduleManagementApi";

const ScheduleModal = ({
  open,
  onClose,
  isEdit = false,
  editScheduleId = null,
  scheduleState = null,
}) => {
  const { id } = useParams();
  // 멤버 리스트 받아오기
  const { data: names } = useQuery(["members", id], () => getTeamMember(id));

  const today = new Date();
  const initialDate = today.toISOString().slice(0, 16);

  const [formValues, setFormValues] = useState({
    projectId: parseInt(id, 10),
    startDate: initialDate,
    endDate: initialDate,
    state: "TBD",
    memberIds: [],
    title: "",
    content: "",
    category: "MILESTONE",
    address: "",
  });

  // 일정 상세 정보 가져오기
  const { data } = useQuery(
    ["ScheduleDetail", editScheduleId, id],
    async () => await getDetailSchedule(id, editScheduleId),
    {
      enabled: isEdit,
      onSuccess: (data) => {
        if (isEdit) {
          const newMembers = data?.members
            .filter((member) => member.in)
            .map((member) => member.id);
          setFormValues({
            projectId: parseInt(id, 10),
            startDate: data.startDate,
            endDate: data.endDate,
            state: scheduleState,
            memberIds: newMembers,
            title: data.title,
            content: data.content,
            category: data.scheduleCategory,
            address: data.scheduleCategory === "MILESTONE" ? "" : data.address,
          });
        }
      },
    }
  );

  // 일정 생성하는 react query 문
  const { mutate: addSchedule, isLoading: addIsLoading } =
    useAddNewScheduleMutation(formValues);

  // 일정 수정하는 react query 문
  const { mutate: modifySchedule, isLoading: modifyIsLoading } =
    useModifyScheduleMutation(editScheduleId, formValues);
  const {
    startDate,
    endDate,
    state,
    memberIds,
    title,
    content,
    category,
    address,
  } = formValues;

  const inputRefs = {
    title: useRef(),
    content: useRef(),
    startDate: useRef(),
    endDate: useRef(),
    address: useRef(),
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
      const updatedParticipants = prevState.memberIds.includes(data.id)
        ? prevState.memberIds.filter((member) => member !== data.id)
        : [...prevState.memberIds, data.id];

      return {
        ...prevState,
        memberIds: updatedParticipants,
      };
    });
  };

  // 일정 생성 또는 수정하기
  const submitNewPlan = () => {
    if (title.length < 2) {
      toast.error("일정 제목을 더 작성해주세요.");
      inputRefs.title.current.focus();
      return;
    }
    const start = new Date(startDate);
    const end = new Date(endDate);
    if (start >= end) {
      toast.error("일정 기간을 수정해주세요.");
      inputRefs.endDate.current.focus();
      return;
    }
    if (category === "MEETING" && start.toDateString() !== end.toDateString()) {
      toast.error("모임의 일정은 같은 일로만 가능합니다.");
      inputRefs.startDate.current.focus();
      inputRefs.endDate.current.focus();
      return;
    }
    if (category === "MEETING" && address.length < 2) {
      toast.error("모임 위치를 더 자세히 작성해주세요.");
      inputRefs.location.current.focus();
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
    console.log(formValues);

    // 일정 생성하기
    if (!isEdit) {
      const { state, ...formData } = formValues;
      addSchedule(formData);
    }

    // 일정 수정하기
    if (isEdit) {
      console.log("수정", formValues);
      modifySchedule(editScheduleId, formValues);
    }

    setFormValues({
      projectId: parseInt(id, 10),
      startDate: initialDate,
      endDate: initialDate,
      state: "TBD",
      memberIds: [],
      title: "",
      content: "",
      category: "MILESTONE",
      location: "",
    });

    onClose();
  };

  return (
    <ModalContainer open={open} onClose={onClose} type="light" width="50%">
      <div className="modal-container">
        <input
          type="text"
          ref={inputRefs.title}
          required
          placeholder="일정 제목을 입력해주세요"
          name="title"
          value={title}
          onChange={handleChange}
        />
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
            {names?.map((data) => (
              <Button
                key={data.id}
                text={data.name}
                size="small"
                type={memberIds.includes(data.id) ? "positive_dark" : ""}
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
              ref={inputRefs.content}
              required
              placeholder="내용을 입력하세요"
              name="content"
              value={content}
              onChange={handleChange}
            />
          </div>
        </div>
        <div className="button-container">
          {!isEdit && !addIsLoading && (
            <Button text="생성 완료" onClick={submitNewPlan} />
          )}
          {isEdit && !modifyIsLoading && (
            <Button text="수정 완료" onClick={submitNewPlan} />
          )}
          {(modifyIsLoading || addIsLoading) && <Button text="로딩 중.." />}
        </div>
      </div>
    </ModalContainer>
  );
};

export default ScheduleModal;
