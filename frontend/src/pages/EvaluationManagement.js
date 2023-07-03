import { useLocation } from "react-router-dom";
import { useQuery } from "react-query";
import { useEffect, useState } from "react";

import { toast } from "react-toastify";

import AllParticipants from "../components/EvaluationManagement/AllParticipants";
import EvaluationRanking from "../components/EvaluationManagement/EvaluationRanking";
import ShowEvaluationChart from "../components/EvaluationManagement/ShowEvaluationChart";

import {
  getFinalEvaluationChartAndRanking,
  getMidEvaluationChartAndRanking,
} from "../lib/apis/evaluationManagementApi";

import { getProjectId } from "../utils/getProjectId";
import { useSelector } from "react-redux";
import { isCompleteProject } from "../utils/isCompleteProject";

const EvaluationManagement = () => {
  // 전역 상태로 변경 필요
  const [isEvaluated, setIsEvaluated] = useState(true);

  const projectId = getProjectId(useLocation());

  const completedProjectIdList = useSelector(
    (state) => state.projectManagement.completedProjectId
  );

  const isCompleted = isCompleteProject(completedProjectIdList, projectId);

  const { data = { charts: [], ranks: [] } } = useQuery(
    isCompleted
      ? ["managementFinalEvaluationChartAndRanking"]
      : ["managementMidEvaluationChartAndRanking"],
    () =>
      isCompleted
        ? getFinalEvaluationChartAndRanking(projectId)
        : getMidEvaluationChartAndRanking(projectId)
  );

  useEffect(() => {
    !isEvaluated && toast.error("평가 페이지는 중간 평가 이후에 공개됩니다.");
  }, [isEvaluated]);

  return data.charts.length ? (
    <div
      className={
        isEvaluated
          ? "evaluation-management"
          : "evaluation-management-non-evaluated"
      }
    >
      <div className="evaluation-management-ranking-wrapper">
        <ShowEvaluationChart
          chartData={data.charts}
          isCompleted={isCompleted}
        />
        <EvaluationRanking rankingData={data.ranks} isCompleted={isCompleted} />
      </div>
      <div className="evaluation-management-participant-wrapper">
        <AllParticipants isCompleted={isCompleted} projectId={projectId} />
      </div>
    </div>
  ) : null;
};

export default EvaluationManagement;
