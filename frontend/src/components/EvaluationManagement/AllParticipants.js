import { useState } from "react";
import { useQuery } from "react-query";

import FinalEvaluation from "./FinalEvaluation";
import ParticipantItem from "./ParticipantItem";

import {
  getEvaluationMember,
  getFinalEvaluation,
} from "../../lib/apis/evaluationManagementApi";
import { useRouter } from "../../hooks/useRouter";

const AllParticipants = ({ projectId, isCompleted }) => {
  const [isFinalEvaluationVisible, setIsFinalEvaluationVisible] = useState("");
  const [finalEvaluationType, setFinalEvaluationType] = useState("");
  const [finalEvalData, setFinalEvalData] = useState("");

  const [badgeQuantity, setBadgeQuantity] = useState();

  const [participantId, setParticipantId] = useState();

  const { routeTo } = useRouter();

  const handleFinalEvaluationModal = async (type, name, finalEvalId) => {
    if (finalEvalId !== -1) {
      const evaluation = await getFinalEvaluation(finalEvalId, projectId);
      setFinalEvalData(evaluation);
    }

    setIsFinalEvaluationVisible(name);
    setFinalEvaluationType(type);
  };

  const handleClickParticipant = (badgeQuantity, participantId) => {
    setParticipantId(participantId);
    setBadgeQuantity(badgeQuantity);
    routeTo(`/profile/${participantId}/introduce`);
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
          isFinalEvaluationVisible={isFinalEvaluationVisible}
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
