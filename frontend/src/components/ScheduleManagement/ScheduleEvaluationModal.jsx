import { useState } from "react";
import { useParams } from "react-router";
import { useMutation } from "react-query";
import { toast } from "react-toastify";

import { useMediaQuery } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";

import Button from "../common/Button";
import ModalContainer from "../common/Modal/ModalContainer";
import AlertCheckModal from "../common/Modal/AlertCheckModal";
import EvaluationMobileContent from "./EvaluationMobileContent";
import EvaluationContent from "./EvaluationContent";

import { getDateTimeDuration } from "../../utils/date";
import { query } from "../../utils/mediaQuery";
import { useScheduleClient } from "../../context/Client/ScheduleClientContext";

const ScheduleEvaluationModal = (props) => {
  const { id } = useParams();
  const isMobile = useMediaQuery(query);

  const { makeNewMidEvaluation } = useScheduleClient();

  const projectId = parseInt(id, 10);
  const scheduleId = parseInt(props.id, 10);

  const [confirmModalVisible, setConfirmModalVisible] = useState(false);
  const [name, setName] = useState(props.members[0].id);
  const [badge, setBadge] = useState("열정적인_참여자");
  const [evaluation, setEvaluation] = useState();

  // 인원 선택 함수
  const participantsClickHandler = (name) => {
    setName(name);
  };
  // 뱃지 선택 함수
  const badgeClickHandler = (badge) => {
    setBadge(badge);
  };

  const time = getDateTimeDuration(props.startDate, props.endDate, props.type);

  // 중간 평가하는 react query 문
  const { mutate } = useMutation(makeNewMidEvaluation, {
    onSuccess: () => {
      toast.success("중간평가 성공하였습니다");
      props.onClose();
    },
    onError: (error) => {
      if (error.response.data.status === 500) {
        toast.error("서버 에러가 발생했습니다. 잠시후에 다시 시도해주세요");
        props.onClose();
      } else if (error.response.data.code === "EVAL_006") {
        toast.error("자기 자신에게 중간평가할 수 없습니다");
      } else if (error.response.data.code === "EVAL_007") {
        toast.error("이미 평가를 진행한 일정입니다");
      } else {
        let message;
        message = error.response.data.message;
        toast.error(message);
      }
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
      projectId: projectId,
      votedId: parseInt(name, 10),
      scheduleId: scheduleId,
      evaluationBadge: badge,
    };
    setEvaluation(data);

    openConfirmModalHandler();
  };

  return (
    <ModalContainer
      open={props.open}
      onClose={props.onClose}
      type="dark"
      width={isMobile ? "100%" : "700px"}
      height={isMobile && "100%"}
      border={isMobile && "0px"}
      padding="25px"
    >
      <AlertCheckModal
        open={confirmModalVisible}
        onClose={closeConfirmModalHandler}
        text="작성 완료한 평가는 수정 또는 삭제할 수 없습니다. 작성 완료 하시겠습니까?"
        clickHandler={() => {
          mutate(evaluation);
        }}
      />
      <div className="schedule-modal">
        <div className="schedule-modal-title">
          <h1>중간 평가 작성</h1>
          {isMobile && (
            <CloseIcon className="mui-icon" onClick={props.onClose} />
          )}
        </div>
        <div className="schedule-modal-content">
          <div className="schedule-modal-content-title">
            <h1>{props.title}</h1>
            <h2>{time} 진행</h2>
          </div>

          {isMobile ? (
            <EvaluationMobileContent
              members={props.members}
              name={name}
              participantsClickHandler={participantsClickHandler}
              badge={badge}
              badgeClickHandler={badgeClickHandler}
            />
          ) : (
            <EvaluationContent
              members={props.members}
              name={name}
              participantsClickHandler={participantsClickHandler}
              badge={badge}
              badgeClickHandler={badgeClickHandler}
            />
          )}
        </div>
        <div className="schedule-modal-button">
          <Button text="작성 완료" onClick={evaluationClickHandler} />
        </div>
      </div>
    </ModalContainer>
  );
};
export default ScheduleEvaluationModal;
