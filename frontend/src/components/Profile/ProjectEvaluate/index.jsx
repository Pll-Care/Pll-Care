import { useEffect } from "react";
import { useLocation } from "react-router-dom";
import BadgeBox from "./BadgeBox";
import FinalEvaluationBox from "./FinalEvaluationBox";

const ProjectEvaluate = () => {
  const { state } = useLocation();
  const { projectId, title } = state;

  useEffect(() => {
    //TODO: 프로젝트 아이디를 가지고 게인페이지 프로젝트 평가 조회 API 호출
  }, []);

  return (
    <div>
      <div className="profile_introduce_titleBox">
        <h1>{title}</h1>
      </div>
      <div className="projectEvaluate">
        <BadgeBox badgeDtos={dummyBadge} />
        <FinalEvaluationBox finalEvals={dummyFinalEvals} />
      </div>
    </div>
  );
};

export default ProjectEvaluate;
const dummyBadge = [
  {
    evaluationBadge: "열정적인_참여자",
    quantity: 0,
  },
  {
    evaluationBadge: "열정적인_참여자",
    quantity: 10,
  },
  {
    evaluationBadge: "열정적인_참여자",
    quantity: 8,
  },
  {
    evaluationBadge: "열정적인_참여자",
    quantity: 6,
  },
  {
    evaluationBadge: "열정적인_참여자",
    quantity: 7,
  },
];

const dummyFinalEvals = [
  {
    memberId: 0,
    memberName: "김호중",
    imageUrl: "",
    content:
      "김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.",
    score: {
      sincerity: 10,
      jobPerformance: 80,
      punctuality: 50,
      communication: 60,
    },
  },
  {
    memberId: 0,
    memberName: "김호중",
    imageUrl: "",
    content:
      "김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.",
    score: {
      sincerity: 10,
      jobPerformance: 80,
      punctuality: 50,
      communication: 60,
    },
  },
  {
    memberId: 0,
    memberName: "김호중",
    imageUrl: "",
    content:
      "김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.",
    score: {
      sincerity: 10,
      jobPerformance: 80,
      punctuality: 50,
      communication: 60,
    },
  },
  {
    memberId: 0,
    memberName: "김호중",
    imageUrl: "",
    content:
      "김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.김호중의 평가에 대한 최종 의견입니다.",
    score: {
      sincerity: 10,
      jobPerformance: 80,
      punctuality: 50,
      communication: 60,
    },
  },
];

/*
{
  "badgeDtos": [
    {
      "evaluationBadge": "열정적인_참여자",
      "quantity": 0
    }
  ],
  "finalEvals": [
    {
      "memberId": 0,
      "memberName": "string",
      "imageUrl": "string",
      "content": "string",
      "score": {
        "sincerity": 0,
        "jobPerformance": 0,
        "punctuality": 0,
        "communication": 0
      }
    }
  ]
}
*/
