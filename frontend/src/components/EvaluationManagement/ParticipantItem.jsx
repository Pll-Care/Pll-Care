import defaultImgUrl from "../../assets/project-default-img.jpg";

import Button from "../common/Button";

import enthusiasticParticipantBadgeImgUrl from "../../assets/enthusiastic-participant-badge-img.svg";
import goodLeaderBadgeImgUrl from "../../assets/good-leader-badge-img.svg";
import ideaBankBadgeImgUrl from "../../assets/idea-bank-badge-img.svg";
import bestSupporterBadgeImgUrl from "../../assets/best-supporter-badge-img.svg";

const ParticipantItem = ({
  member,
  handleFinalEvaluationModal,
  handleClickParticipant,
  setBadgeQuantity,
  setParticipantId,
  isCompleted,
  isSelf,
  finalEvalId,
}) => {
  const getBadgeImgUrl = (idx) => {
    switch (idx) {
      case 0:
        return bestSupporterBadgeImgUrl;
      case 1:
        return enthusiasticParticipantBadgeImgUrl;
      case 2:
        return goodLeaderBadgeImgUrl;
      case 3:
        return ideaBankBadgeImgUrl;
      default:
        return "";
    }
  };

  return (
    <div
      onClick={() => handleClickParticipant(member.memberId)}
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
                    <figure
                      style={{
                        backgroundImage: `url(${getBadgeImgUrl(idx)})`,
                      }}
                    />
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
            onClick={(e) => {
              e.stopPropagation();
              setBadgeQuantity(member.badgeDtos);
              setParticipantId(member.memberId);
              handleFinalEvaluationModal("evaluation", member.name, -1);
            }}
          />
        )}
        {isCompleted && !isSelf && finalEvalId && (
          <Button
            text={"최종 평가보기"}
            size={"small"}
            type={"positive"}
            color={"white"}
            onClick={(e) => {
              e.stopPropagation();
              setBadgeQuantity(member.badgeDtos);
              setParticipantId(member.memberId);
              handleFinalEvaluationModal(
                "showEvaluation",
                member.name,
                finalEvalId
              );
            }}
          />
        )}
      </div>
    </div>
  );
};

export default ParticipantItem;
