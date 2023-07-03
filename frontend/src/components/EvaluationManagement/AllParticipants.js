import { useState } from "react";
import { useQuery } from "react-query";

import FinalEvaluation from "./FinalEvaluation";
import ParticipantItem from "./ParticipantItem";

import { getEvaluationMember } from "../../lib/apis/evaluationManagementApi";

const AllParticipants = ({ projectId, isCompleted }) => {
  const [isFinalEvaluationVisible, setIsFinalEvaluationVisible] = useState("");

  const [badgeQuantity, setBadgeQuantity] = useState();

  const [participantId, setParticipantId] = useState();

  const handleFinalEvaluationModal = (name) => {
    setIsFinalEvaluationVisible(name);
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
            member={member}
            handleFinalEvaluationModal={handleFinalEvaluationModal}
            handleClickParticipant={handleClickParticipant}
            isCompleted={isCompleted}
            isSelf={member.me}
          />
        ))}
      </div>
      {isFinalEvaluationVisible && (
        <FinalEvaluation
          badgeQuantity={badgeQuantity}
          participantId={participantId}
          member={isFinalEvaluationVisible}
          setIsFinalEvaluationVisible={setIsFinalEvaluationVisible}
        />
      )}
    </div>
  );
};

export default AllParticipants;
