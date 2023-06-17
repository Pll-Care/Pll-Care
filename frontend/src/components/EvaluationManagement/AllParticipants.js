import { useState } from "react";
import Button from "../../components/shared/Button";
import FinalEvaluation from "./FinalEvaluation";

const AllParticipants = ({ getSortedAverageEvaluationList }) => {
  const [isFinalEvaluationVisible, setIsFinalEvaluationVisible] = useState("");

  const handleFinalEvaluationModal = (name) => {
    setIsFinalEvaluationVisible(name);
  };

  return (
    <div className="evaluation-management-all-participants">
      <h1>참여자 보기</h1>
      <div className="evaluation-management-participants">
        {getSortedAverageEvaluationList().map((item) => (
          <div className="evaluation-management-participant">
            <div className="evaluation-management-participant-left-col">
              <figure className="evaluation-management-user-profile" />
            </div>
            <div className="evaluation-management-participant-right-col">
              <div>{item.name}</div>
              <Button
                text={"최종 평가하기"}
                onClick={() => handleFinalEvaluationModal(item.name)}
              />
              <div className="evaluation-management-badges">
                <figure />
                <figure />
                <figure />
              </div>
            </div>
          </div>
        ))}
      </div>
      {isFinalEvaluationVisible && (
        <FinalEvaluation
          member={isFinalEvaluationVisible}
          setIsFinalEvaluationVisible={setIsFinalEvaluationVisible}
        />
      )}
    </div>
  );
};

export default AllParticipants;
