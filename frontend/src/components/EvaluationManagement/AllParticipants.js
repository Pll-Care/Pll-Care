import { useState } from "react";
import { useQuery } from "react-query";

import FinalEvaluation from "./FinalEvaluation";
import ParticipantItem from "./ParticipantItem";

import {
  getEvaluationMember,
  getFinalEvaluation,
} from "../../lib/apis/evaluationManagementApi";

const AllParticipants = ({ projectId, isCompleted }) => {
  const [isFinalEvaluationVisible, setIsFinalEvaluationVisible] = useState("");
  const [finalEvaluationType, setFinalEvaluationType] = useState("");
  const [finalEvalData, setFinalEvalData] = useState("");

  const [badgeQuantity, setBadgeQuantity] = useState();

  const [participantId, setParticipantId] = useState();

  const handleFinalEvaluationModal = async (type, name, finalEvalId) => {
    if (finalEvalId !== -1) {
      const evaluation = await getFinalEvaluation(finalEvalId);
      setFinalEvalData(evaluation);
    }

    setIsFinalEvaluationVisible(name);
    setFinalEvaluationType(type);
  };

  const handleClickParticipant = (badgeQuantity, participantId) => {
    setParticipantId(participantId);
    setBadgeQuantity(badgeQuantity);
  };

  const { data: memberList = [] } = useQuery(
    ["managementEvaluationAllParticipants"],
    () => getEvaluationMember(projectId)
  );

  return (
    <div className="evaluation-management-all-participants">
      <h1>참여자 보기</h1>
      <div className="evaluation-management-participants">
        {memberList.map((member) => (
          <ParticipantItem
            key={member.id}
            member={member}
            handleFinalEvaluationModal={handleFinalEvaluationModal}
            handleClickParticipant={() =>
              handleClickParticipant(member.badgeDtos, member.id)
            }
            isCompleted={isCompleted}
            isSelf={member.me}
            finalEvalId={member.finalEvalId}
          />
        ))}
      </div>
      {isFinalEvaluationVisible && (
        <FinalEvaluation
          type={finalEvaluationType}
          badgeQuantity={badgeQuantity}
          participantId={participantId}
          member={isFinalEvaluationVisible}
          setIsFinalEvaluationVisible={setIsFinalEvaluationVisible}
          data={finalEvalData}
        />
      )}
    </div>
  );
};

export default AllParticipants;
