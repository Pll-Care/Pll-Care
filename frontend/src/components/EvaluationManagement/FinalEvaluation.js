import { useRef, useState } from "react";
import Button from "../../components/common/Button";
import { getProjectId } from "../../utils/getProjectId";
import { useLocation } from "react-router-dom";
import { useSelector } from "react-redux";
import { isCompleteProject } from "../../utils/isCompleteProject";
import useEvaluationManagementMutation from "../../hooks/useEvaluationManagementMutation";
import { toast } from "react-toastify";

const evaluationCriterion = [
  {
    name: "성실도",
    value: "sincerity",
  },
  {
    name: "시간 엄수",
    value: "punctuality",
  },
  {
    name: "업무 수행 능력",
    value: "jobPerformance",
  },
  {
    name: "의사 소통",
    value: "communication",
  },
];

const evaluationOptionList = [
  {
    id: 0,
    value: 0,
    name: "0",
  },
  {
    id: 1,
    value: 1,
    name: "1",
  },
  {
    id: 2,
    value: 2,
    name: "2",
  },
  {
    id: 3,
    value: 3,
    name: "3",
  },
  {
    id: 4,
    value: 4,
    name: "4",
  },
  {
    id: 5,
    value: 5,
    name: "5",
  },
];

const FinalEvaluation = ({
  type,
  participantId,
  member,
  setIsFinalEvaluationVisible,
  badgeQuantity,
  data,
}) => {
  const [evaluationScore, setEvaluationScore] = useState([
    {
      name: "diligence",
      value: 0,
    },
    {
      name: "punctuality",
      value: 0,
    },
    {
      name: "performance",
      value: 0,
    },
    {
      name: "communication",
      value: 0,
    },
  ]);

  const [content, setContent] = useState("");

  const modalOutside = useRef();

  const projectId = getProjectId(useLocation());

  const completedProjectIdList = useSelector(
    (state) => state.projectManagement.completedProjectId
  );

  const isCompleted = isCompleteProject(completedProjectIdList, projectId);

  const { finalEvaluationMutate } = useEvaluationManagementMutation();

  const handleFinalEvaluationVisible = (e) => {
    if (e.target === modalOutside.current) {
      setIsFinalEvaluationVisible("");
    }
  };

  const handleChangeContent = (e) => {
    setContent(e.target.value);
  };

  const handleChangeScore = (e, idx) => {
    setEvaluationScore((prevScore) => {
      return prevScore.map((score, index) =>
        idx === index ? { ...score, value: parseInt(e.target.value) } : score
      );
    });
  };

  const handleSubmitFinalEvaluation = () => {
    if (content.length < 1) {
      toast.error("최종 의견을 작성해주세요.");
      return;
    }

    finalEvaluationMutate({
      projectId: projectId,
      evaluatedId: participantId,
      score: {
        sincerity: evaluationScore[0].value,
        jobPerformance: evaluationScore[1].value,
        punctuality: evaluationScore[2].value,
        communication: evaluationScore[3].value,
      },
      content: content,
      state: isCompleted,
    });

    setIsFinalEvaluationVisible(false);
  };

  return (
    <div
      className="final-evaluation-wrapper"
      ref={modalOutside}
      onClick={handleFinalEvaluationVisible}
    >
      {type === "evaluation" ? (
        <div className="evaluation-management-final-evaluation">
          <div className="final-evaluation-heading">
            '{member}' 최종 평가하기
          </div>
          <div className="final-evaluation-body">
            <div className="evaluation">
              <h1>평가하기</h1>
              <div className="evaluation-criterion">
                {evaluationCriterion.map((criterion, idx) => (
                  <div key={idx}>
                    {criterion.name}
                    <select
                      className="evaluation-score"
                      onChange={(e) => handleChangeScore(e, idx)}
                      value={evaluationScore[idx].value}
                    >
                      {evaluationOptionList.map((opt) => (
                        <option key={opt.value} value={opt.value}>
                          {opt.name}
                          <figure />
                        </option>
                      ))}
                    </select>
                  </div>
                ))}
              </div>
            </div>
            <div className="badges">
              <h1>누적 배지</h1>
              <div className="badges-body">
                {badgeQuantity?.map((badge) => (
                  <div className="badge">
                    <figure />
                    <div>{badge.evaluationBadge}</div>
                    <div>{badge.quantity} 개</div>
                  </div>
                ))}
              </div>
            </div>
            <div className="content">
              <h1>최종 의견 작성하기</h1>
              <div>
                <div>'{member}'에 대한 최종 의견입니다.</div>
                <textarea
                  value={content}
                  onChange={handleChangeContent}
                  placeholder={`${member}에 대한 최종 평가를 글로 써보세요!`}
                />
              </div>
            </div>
          </div>
          <div className="final-evaluation-button-wrapper">
            <Button
              text={"평가 완료하기"}
              onClick={handleSubmitFinalEvaluation}
            />
          </div>
        </div>
      ) : (
        <div className="evaluation-management-final-evaluation">
          <div className="final-evaluation-heading">
            '{member}' 최종 평가 보기
          </div>
          <div className="final-evaluation-body">
            <div className="evaluation">
              <h1>평가</h1>
              <div className="evaluation-criterion">
                {evaluationCriterion.map((criterion, idx) => (
                  <div key={idx}>
                    {criterion.name}
                    <div>{data.score[criterion.value]} 점</div>
                  </div>
                ))}
              </div>
            </div>
            <div className="badges">
              <h1>누적 배지</h1>
              <div className="badges-body">
                {badgeQuantity?.map((badge) => (
                  <div className="badge">
                    <figure />
                    <div>{badge.evaluationBadge}</div>
                    <div>{badge.quantity} 개</div>
                  </div>
                ))}
              </div>
            </div>
            <div className="content">
              <h1>최종 의견</h1>
              <div>
                <div>'{member}'에 대한 최종 의견입니다.</div>
                <textarea value={data.content} readOnly />
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default FinalEvaluation;
