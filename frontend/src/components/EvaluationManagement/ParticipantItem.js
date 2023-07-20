import defaultImgUrl from "../../assets/project-default-img.jpg";

import Button from "../../components/common/Button";

const ParticipantItem = ({
  member,
  handleFinalEvaluationModal,
  handleClickParticipant,
  isCompleted,
  isSelf,
  finalEvalId
}) => {
  return (
    <div
      onClick={() => handleClickParticipant(member.badgeDtos, member.id)}
      className="evaluation-management-participant"
    >
      <div className="evaluation-management-participant-left-col">
        <figure
          style={{
            backgroundImage: `url(${
              member.imageUrl ? member.imageUrl : defaultImgUrl
            })`,
          }}
          className="evaluation-management-user-profile"
        />
      </div>
      <div className="evaluation-management-participant-right-col">
        <div className="name-badge-wrapper">
          <div>{member.name}</div>
          <div className="evaluation-management-badges">
            <div className="badge-quantity-container">
              {member.badgeDtos?.map((badge, idx) =>
                badge.quantity ? (
                  <div
                    key={idx}
                    className={[`badge-quantity`, `badge-quantity_${idx}`].join(
                      " "
                    )}
                  >
                    <figure />
                    <div>X{badge.quantity}</div>
                  </div>
                ) : null
              )}
            </div>
          </div>
        </div>
        {isCompleted && !isSelf && !finalEvalId && (
          <Button
            text={"최종 평가하기"}
            size={"small"}
            onClick={() => handleFinalEvaluationModal("evaluation", member.name, -1)}
          />
        )}
        {isCompleted && !isSelf && finalEvalId && (
          <Button
            text={"최종 평가보기"}
            size={"small"}
            type={"positive"}
            onClick={() => handleFinalEvaluationModal("showEvaluation", member.name, finalEvalId)}
          />
        )}
      </div>
    </div>
  );
};

export default ParticipantItem;
