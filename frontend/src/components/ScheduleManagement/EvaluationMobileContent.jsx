import { badges } from "../../utils/evaluation";
import Button from "../common/Button";

const EvaluationMobileContent = ({
  members,
  name,
  participantsClickHandler,
  badge,
  badgeClickHandler,
}) => {
  return (
    <div className="schedule-modal-mobile">
      <h3>참여자 선택</h3>
      <div className="schedule-modal-content-evaluation-member">
        {members?.map((member, index) => (
          <Button
            key={index}
            text={member.name}
            size="small"
            type={name === member.id ? "positive_dark" : ""}
            onClick={() => participantsClickHandler(member.id)}
          />
        ))}
      </div>
      <h3>뱃지 선택</h3>
      <div className="schedule-modal-mobile-badge">
        {badges?.map((b) => (
          <div className="schedule-modal-mobile-badge-item" key={b.name}>
            <div
              className={`modal-badge ${badge === b.value ? "selected" : ""}`}
              key={b.name}
              onClick={() => badgeClickHandler(b.value)}
            >
              <img src={b.image} alt={b.name} key={b.name} />
            </div>
            <h5>{b.name}</h5>
          </div>
        ))}
      </div>
    </div>
  );
};
export default EvaluationMobileContent;
