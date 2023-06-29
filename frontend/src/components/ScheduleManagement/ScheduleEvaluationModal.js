import { useState } from "react";
import { useDispatch } from "react-redux";
import { useParams } from "react-router";
import { useMutation } from "react-query";
import { toast } from "react-toastify";

import { Tooltip } from "@mui/material";

import { addEvaluation } from "../../redux/evaluationManagementSlice";
import Button from "../common/Button";
import ModalContainer from "../common/ModalContainer";
import { getDateTimeDuration } from "../../utils/date";
import { makeNewMidEvaluation } from "../../lib/apis/evaluationManagementApi";
import AlertModal from "./AlertModal";

const ScheduleEvaluationModal = (props) => {
  const dispatch = useDispatch();
  const { id } = useParams();

  const [confirmModalVisible, setConfirmModalVisible] = useState(false);
  const [name, setName] = useState(props.members[0].id);
  const [badge, setBadge] = useState("열정적인_참여자");
  const [evaluation, setEvaluation] = useState();
  console.log(name);

  const participantsClickHandler = (name) => {
    setName(name);
  };
  const badgeClickHandler = (badge) => {
    setBadge(badge);
  };

  const time = getDateTimeDuration(props.startDate, props.endDate, props.type);

  const { mutate } = useMutation(makeNewMidEvaluation, {
    onSuccess: () => {
      props.onClose();
      toast.success("중간평가 성공하였습니다");
    },
  });

  const openConfirmModalHandler = () => {
    setConfirmModalVisible(true);
  };
  const closeConfirmModalHandler = () => {
    setConfirmModalVisible(false);
  };

  const evaluationClickHandler = () => {
    const data = {
      projectId: parseInt(id, 10),
      votedId: parseInt(name, 10),
      scheduleId: parseInt(props.id, 10),
      evaluationBadge: badge,
    };
    if (data.votedId < 0) {
      toast.error("멤버를 골라주세요");
      return;
    }
    const badges = [
      "열정적인_참여자",
      "아이디어_뱅크",
      "탁월한_리더",
      "최고의_서포터",
    ];
    if (!badges.includes(data.evaluationBadge)) {
      toast.error("뱃지를 골라주세요");
    }
    setEvaluation(data);
    console.log(data);
    openConfirmModalHandler();
    //mutate(data);
    //console.log(newEvaluation);
    //dispatch(addEvaluation(newEvaluation));
  };

  return (
    <ModalContainer
      open={props.open}
      onClose={props.onClose}
      type="dark"
      width="40%"
    >
      <AlertModal
        open={confirmModalVisible}
        onClose={closeConfirmModalHandler}
        width="30%"
        text="작성 완료한 평가는 수정 또는 삭제할 수 없습니다. 작성 완료 하시겠습니까?"
        clickHandler={() => {
          mutate(evaluation);
          closeConfirmModalHandler();
        }}
      />
      <div className="schedule-modal">
        <h1>평가 작성</h1>
        <div className="schedule-modal-content">
          <h1>{props.title}</h1>
          <h2>{time} 진행</h2>
          <div className="schedule-modal-content-evaluation">
            <div className="modal-member">
              <h3>참여자</h3>
              {props.members.map((member, index) => (
                <Button
                  key={index}
                  text={member.name}
                  size="small"
                  type={name === member.id ? "positive_dark" : ""}
                  onClick={() => participantsClickHandler(member.id)}
                />
              ))}
            </div>
            <div className="modal-badges">
              <h3>뱃지 선택</h3>
              <div className="modal-badges-items">
                <Tooltip title="열정적인 참여자">
                  <div
                    className={`modal-badge ${
                      badge === "열정적인_참여자" ? "selected" : ""
                    }`}
                    onClick={() => badgeClickHandler("열정적인_참여자")}
                  >
                    🔥
                  </div>
                </Tooltip>
                <Tooltip title="아이디어 뱅크">
                  <div
                    className={`modal-badge ${
                      badge === "아이디어_뱅크" ? "selected" : ""
                    }`}
                    onClick={() => badgeClickHandler("아이디어_뱅크")}
                  >
                    💡
                  </div>
                </Tooltip>
                <Tooltip title="탁월한 리더">
                  <div
                    className={`modal-badge ${
                      badge === "탁월한_리더" ? "selected" : ""
                    }`}
                    onClick={() => badgeClickHandler("탁월한_리더")}
                  >
                    👏
                  </div>
                </Tooltip>
                <Tooltip title="최고의 서포터">
                  <div
                    className={`modal-badge ${
                      badge === "최고의_서포터" ? "selected" : ""
                    }`}
                    onClick={() => badgeClickHandler("최고의_서포터")}
                  >
                    👥
                  </div>
                </Tooltip>
              </div>
            </div>
          </div>
        </div>
        <div className="schedule-modal-button">
          <Button text="작성 완료" onClick={evaluationClickHandler} />
          <Button text="취소" onClick={() => props.onClose()} />
        </div>
      </div>
    </ModalContainer>
  );
};
export default ScheduleEvaluationModal;
