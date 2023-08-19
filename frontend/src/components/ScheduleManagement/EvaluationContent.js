import { Tooltip } from "@mui/material";
import { badges } from "../../utils/evaluation";
import Button from "../common/Button";

const EvaluationContent = ({
  members,
  name,
  participantsClickHandler,
  badge,
  badgeClickHandler,
}) => {
  return (
    <div className="schedule-modal-content-evaluation">
      <h3>참여자</h3>
      <div className="schedule-modal-content-evaluation-member">
        {members.map((member, index) => (
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
      <div className="schedule-modal-content-evaluation-badge">
        {badges.map((b) => (
          <Tooltip title={b.name} key={b.name}>
            <div
              className={`modal-badge ${badge === b.name ? "selected" : ""}`}
              key={b.name}
              onClick={() => badgeClickHandler(b.name)}
            >
              <img src={b.image} alt={b.name} key={b.name} />
            </div>
          </Tooltip>
        ))}
      </div>
    </div>
  );
};
export default EvaluationContent;
