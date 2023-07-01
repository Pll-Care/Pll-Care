import { useState } from "react";
import { useQuery } from "react-query";
import { useLocation } from "react-router-dom";

import Button from "../../components/common/Button";
import FinalEvaluation from "./FinalEvaluation";
import { getProjectId } from "../../utils/getProjectId";

import defaultImgUrl from "../../assets/project-default-img.jpg";

import { getEvaluationMember } from "../../lib/apis/evaluationManagementApi";

const AllParticipants = () => {
  // 전역 상태로 처리 필요
  const [isCompleted, setIsCompleted] = useState(true);

  const [isFinalEvaluationVisible, setIsFinalEvaluationVisible] = useState("");

  const [participantId, setParticipantId] = useState();

  const handleFinalEvaluationModal = (name) => {
    setIsFinalEvaluationVisible(name);
  };

  const handleClickParticipant = (participantId) => {
    setParticipantId(participantId);
  };

  const projectId = getProjectId(useLocation());

  const { data: memberList = [] } = useQuery(
    ["managementEvaluationAllParticipants"],
    () => getEvaluationMember(projectId)
  );

  return (
    <div className="evaluation-management-all-participants">
      <h1>참여자 보기</h1>
      <div className="evaluation-management-participants">
        {memberList.map((item, idx) => (
          <div
            key={item.id}
            onClick={() => handleClickParticipant(item.id)}
            className="evaluation-management-participant"
          >
            <div className="evaluation-management-participant-left-col">
              <figure
                style={{
                  backgroundImage: `url(${
                    item.imageUrl ? item.imageUrl : defaultImgUrl
                  })`,
                }}
                className="evaluation-management-user-profile"
              />
            </div>
            <div className="evaluation-management-participant-right-col">
              <div className="name-badge-wrapper">
                <div>{item.name}</div>
                <div className="evaluation-management-badges">
                  <div className="badge-quantity-container">
                    {item.badgeDtos?.map((badge, idx) =>
                      badge.quantity ? (
                        <div
                          key={item.id}
                          className={[
                            `badge-quantity`,
                            `badge-quantity_${idx}`,
                          ].join(" ")}
                        >
                          <figure />
                          <div>X{badge.quantity}</div>
                        </div>
                      ) : null
                    )}
                  </div>
                </div>
              </div>
              {isCompleted && (
                <Button
                  text={"최종 평가하기"}
                  size={"small"}
                  onClick={() => handleFinalEvaluationModal(item.name)}
                />
              )}
            </div>
          </div>
        ))}
      </div>
      {isFinalEvaluationVisible && (
        <FinalEvaluation
          participantId={participantId}
          member={isFinalEvaluationVisible}
          setIsFinalEvaluationVisible={setIsFinalEvaluationVisible}
        />
      )}
    </div>
  );
};

export default AllParticipants;
