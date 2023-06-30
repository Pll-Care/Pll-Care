import { useState } from "react";
import Button from "../../components/common/Button";
import FinalEvaluation from "./FinalEvaluation";

const AllParticipants = () => {
  const memberList = [
    {
      id: 11,
      name: "홍서현",
      imageUrl: "string",
      badgeDtos: [
        {
          evaluationBadge: "탁월한_리더",
          quantity: 2,
        },
        {
          evaluationBadge: "열정적인_참여자",
          quantity: 3,
        },
        {
          evaluationBadge: "최고의_서포터",
          quantity: 1,
        },
        {
          evaluationBadge: "아이디어_뱅크",
          quantity: 1,
        },
      ],
      finalEvalId: 0,
    },
    {
      id: 12,
      name: "이연제",
      imageUrl: "string",
      badgeDtos: [
        {
          evaluationBadge: "탁월한_리더",
          quantity: 15,
        },
        {
          evaluationBadge: "열정적인_참여자",
          quantity: 1,
        },
        {
          evaluationBadge: "최고의_서포터",
          quantity: 0,
        },
        {
          evaluationBadge: "아이디어_뱅크",
          quantity: 0,
        },
      ],
      finalEvalId: 0,
    },
    {
      id: 13,
      name: "조상욱",
      imageUrl: "string",
      badgeDtos: [
        {
          evaluationBadge: "탁월한_리더",
          quantity: 2,
        },
        {
          evaluationBadge: "열정적인_참여자",
          quantity: 6,
        },
        {
          evaluationBadge: "최고의_서포터",
          quantity: 1,
        },
        {
          evaluationBadge: "아이디어_뱅크",
          quantity: 0,
        },
      ],
      finalEvalId: 0,
    },
  ];

  // 전역 상태로 처리 필요
  const [isCompleted, setIsCompleted] = useState(true);

  const [isFinalEvaluationVisible, setIsFinalEvaluationVisible] = useState("");

  const handleFinalEvaluationModal = (name) => {
    setIsFinalEvaluationVisible(name);
  };

  return (
    <div className="evaluation-management-all-participants">
      <h1>참여자 보기</h1>
      <div className="evaluation-management-participants">
        {memberList.map((item) => (
          <div className="evaluation-management-participant">
            <div className="evaluation-management-participant-left-col">
              <figure className="evaluation-management-user-profile" />
            </div>
            <div className="evaluation-management-participant-right-col">
              <div className="name-badge-wrapper">
                <div>{item.name}</div>
                <div className="evaluation-management-badges">
                  <div className="badge-quantity-container">
                    {item.badgeDtos?.map((badge, idx) =>
                      badge.quantity ? (
                        <div
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
          member={isFinalEvaluationVisible}
          setIsFinalEvaluationVisible={setIsFinalEvaluationVisible}
        />
      )}
    </div>
  );
};

export default AllParticipants;
