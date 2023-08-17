import React, { useState, useRef } from "react";
import { useQuery } from "react-query";
import { toast } from "react-toastify";

import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { useMediaQuery } from "@mui/material";

import ModalContainer from "../common/ModalContainer";
import Button from "../common/Button";
import AlertCheckModal from "../common/AlertCheckModal";

import { getDetailSchedule } from "../../lib/apis/scheduleManagementApi";
import { getDateTimeDuration } from "../../utils/date";
import {
  useDeleteScheduleMutation,
  useModifyScheduleMutation,
} from "../../hooks/useScheduleManagementMutation";
import { query } from "../../utils/mediaQuery";

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

  const isMobile = useMediaQuery(query);

  const [isEdit, setIsEdit] = useState(false);
  const [deleteModalVisible, setDeleteModalVisible] = useState(false);

  const deleteBody = {
    scheduleId: scheduleId,
    projectId: projectId,
  };

  // 삭제 처리하는 react query 문
  const { mutate: deleteSchedule } = useDeleteScheduleMutation(deleteBody);

  // 수정 처리하는 react query 문
  const { mutate: modifySchedule } = useModifyScheduleMutation(formValues);

  // 삭제 모달
  const hideDeleteModalHandler = () => {
    setDeleteModalVisible(false);
    onClose();
  };

  // 일정 상세 조회
  const { data } = useQuery(
    ["scheduleDetail", projectId, scheduleId],
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
  const { title, content, category, address, startDate, endDate, memberIds } =
    formValues;

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

  // 멤버 클릭했을 때
  const handleMemberClick = (data) => {
    setFormValues((prevFormValues) => {
      const updatedMemberIds = prevFormValues.memberIds.map((member) => {
        if (member.id === data.id) {
          return {
            ...member,
            in: !member.in,
          };
        }
        return member;
      });

      return {
        ...prevFormValues,
        memberIds: updatedMemberIds,
      };
    });
  };

  const time = getDateTimeDuration(startDate, endDate, category);

  // 수정 취소
  const handleCancelModify = () => {
    setIsEdit((prevState) => !prevState);
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
  };

  // 수정 완료
  const handleCompleteModify = () => {
    const { memberIds, ...formData } = formValues;
    const memberData = memberIds
      .filter((member) => member.in)
      .map((member) => member.id);

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
    if (memberData.length < 1) {
      toast.error("일정 참여할 멤버를 선택해주세요.");
      return;
    }
    if (content.length < 2) {
      toast.error("일정 내용 설명을 더 작성해주세요.");
      inputRefs.content.current.focus();
      return;
    }

    const body = { ...formData, memberIds: memberData };

    modifySchedule(body);

    setFormValues({
      projectId: projectId,
      scheduleId: scheduleId,
      startDate: "",
      endDate: "",
      state: scheduleState,
      memberIds: [],
      title: "",
      content: "",
      category: "MILESTONE",
      location: "",
    });

    setIsEdit((prevState) => !prevState);

    onClose();
  };

  return (
    <ModalContainer
      open={open}
      onClose={onClose}
      width={isMobile ? "100%" : "50%"}
      height={isMobile && "100%"}
      border={isMobile && "0px"}
      padding="15px"
    >
      <AlertCheckModal
        open={deleteModalVisible}
        onClose={hideDeleteModalHandler}
        text="정말 일정 삭제하시겠습니까?"
        clickHandler={() => deleteSchedule(deleteBody)}
      />
      <div className="schedule-detail-mobile">
        {isMobile && (
          <>
            {isEdit ? (
              <div className="schedule-detail-modal-title">
                <ArrowBackIcon
                  className="mui-icon"
                  onClick={handleCancelModify}
                />
                <input
                  type="text"
                  ref={inputRefs.title}
                  required
                  placeholder="일정 제목을 입력해주세요"
                  name="title"
                  value={title}
                  onChange={handleChange}
                />
              </div>
            ) : (
              <h1>{title}</h1>
            )}
          </>
        )}

        <div className="schedule-detail-modal">
          {!isMobile &&
            (isEdit ? (
              <div className="schedule-detail-modal-title">
                <ArrowBackIcon
                  className="mui-icon"
                  onClick={handleCancelModify}
                />
                <input
                  type="text"
                  ref={inputRefs.title}
                  required
                  placeholder="일정 제목을 입력해주세요"
                  name="title"
                  value={title}
                  onChange={handleChange}
                />
              </div>
            ) : (
              <h1>{title}</h1>
            ))}

          <div className="schedule-detail-modal-content">
            {/*첫번째 행 진행일시*/}
            <h5>진행일시</h5>
            {isEdit ? (
              <div className="schedule-detail-modal-content-time">
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
            ) : (
              <h5>{time}</h5>
            )}
            {/*두번째 행 참여자*/}
            <h5>참여자</h5>
            <div className="schedule-detail-modal-member">
              {isEdit && (
                <div className="schedule-detail-modal-member-buttons">
                  {memberIds.map((member, index) => (
                    <Button
                      text={member.name}
                      key={index}
                      type={member.in ? "positive_dark" : ""}
                      size="small"
                      onClick={() => handleMemberClick(member)}
                    />
                  ))}
                </div>
              )}
              {!isEdit && (
                <>
                  {data?.members.map(
                    (member) =>
                      member.in && <h6 key={member.id}>{member.name + " "} </h6>
                  )}
                </>
              )}
            </div>
            {/*세번째 행 장소*/}
            {category === "MEETING" && <h5>장소</h5>}
            {category === "MEETING" &&
              (isEdit ? (
                <input
                  type="text"
                  ref={inputRefs.address}
                  required
                  placeholder="장소를 입력하세요"
                  name="address"
                  value={address}
                  onChange={handleChange}
                />
              ) : (
                <h5>{address}</h5>
              ))}

            <h5>내용</h5>
            {isEdit ? (
              <input
                type="text"
                ref={inputRefs.content}
                required
                placeholder="내용을 입력하세요"
                name="content"
                value={content}
                onChange={handleChange}
              />
            ) : (
              <h5>{content}</h5>
            )}
          </div>

          <div className="schedule-detail-modal-button">
            {data?.deleteAuthorization && (
              <Button
                text="삭제하기"
                type="underlined"
                size="small"
                onClick={() => setDeleteModalVisible((prevState) => !prevState)}
              />
            )}
            <Button
              text={isEdit ? "수정완료" : "수정하기"}
              size="small"
              type="positive"
              onClick={
                isEdit
                  ? handleCompleteModify
                  : () => setIsEdit((prevState) => !prevState)
              }
            />
          </div>
        </div>
      </div>
    </ModalContainer>
  );
};
export default ScheduleDetailModal;
