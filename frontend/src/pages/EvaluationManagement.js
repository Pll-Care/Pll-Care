import { useLocation } from "react-router-dom";
import AllParticipants from "../components/EvaluationManagement/AllParticipants";
import EvaluationRanking from "../components/EvaluationManagement/EvaluationRanking";
import ShowEvaluationChart from "../components/EvaluationManagement/ShowEvaluationChart";
import { getMidEvaluationChartAndRanking } from "../lib/apis/evaluationManagementApi";
import { useQuery } from "react-query";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getProjectId } from "../utils/getProjectId";

const mockList = {
  charts: [
    {
      memberId: 11,
      name: "홍서현",
      evaluation: [
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
    },
    {
      memberId: 12,
      name: "이연제",
      evaluation: [
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
    },
    {
      memberId: 13,
      name: "조상욱",
      evaluation: [
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
    },
    {
      memberId: 11,
      name: "홍서현",
      evaluation: [
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
    },
    {
      memberId: 12,
      name: "이연제",
      evaluation: [
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
    },
    {
      memberId: 13,
      name: "조상욱",
      evaluation: [
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
    },
    {
      memberId: 11,
      name: "홍서현",
      evaluation: [
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
    },
    {
      memberId: 12,
      name: "이연제",
      evaluation: [
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
    },
    {
      memberId: 13,
      name: "조상욱",
      evaluation: [
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
    },
  ],
  ranks: [
    {
      rank: 3,
      memberId: 11,
      name: "홍서현",
      quantity: 7,
    },
    {
      rank: 1,
      memberId: 12,
      name: "이연제",
      quantity: 16,
    },
    {
      rank: 2,
      memberId: 13,
      name: "조상욱",
      quantity: 9,
    },
  ],
};

const EvaluationManagement = () => {
  // 전역 상태로 변경 필요
  const [isEvaluated, setIsEvaluated] = useState(true);

  const projectId = getProjectId(useLocation());

  const { data } = useQuery(["managementEvaluationChartAndRanking"], () =>
    getMidEvaluationChartAndRanking(projectId)
  );

  useEffect(() => {
    !isEvaluated && toast.error("평가 페이지는 중간 평가 이후에 공개됩니다.");
  }, []);

  return (
    <div
      className={
        isEvaluated
          ? "evaluation-management"
          : "evaluation-management-non-evaluated"
      }
    >
      <div className="evaluation-management-ranking-wrapper">
        <ShowEvaluationChart chartData={mockList.charts} />
        <EvaluationRanking rankingData={mockList.ranks} />
      </div>
      <div className="evaluation-management-participant-wrapper">
        <AllParticipants />
      </div>
    </div>
  );
};

export default EvaluationManagement;
