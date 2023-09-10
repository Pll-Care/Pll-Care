import { useLocation } from "react-router-dom";
import { useQuery } from "react-query";

import AllParticipants from "../components/EvaluationManagement/AllParticipants";
import EvaluationRanking from "../components/EvaluationManagement/EvaluationRanking";
import ShowEvaluationChart from "../components/EvaluationManagement/ShowEvaluationChart";

import { getProjectId } from "../utils/getProjectId";
import { useManagementClient } from "../context/Client/ManagementClientContext";

const EvaluationManagement = () => {
  const projectId = getProjectId(useLocation());

  const {
    getCompleteProjectData,
    getFinalEvaluationChartAndRanking,
    getMidEvaluationChartAndRanking,
  } = useManagementClient();

  const { data: isCompleted } = useQuery(
    ["completeProjectData", projectId],
    () => getCompleteProjectData(projectId)
  );

  const { data = { charts: [], ranks: [] } } = useQuery(
    isCompleted
      ? ["managementFinalEvaluationChartAndRanking", projectId]
      : ["managementMidEvaluationChartAndRanking", projectId],
    () =>
      isCompleted
        ? getFinalEvaluationChartAndRanking(projectId)
        : getMidEvaluationChartAndRanking(projectId)
  );

  return data.charts.length ? (
    <div className="evaluation-management">
      <div
        className={
          !isCompleted
            ? data.ranks.length
              ? "evaluation-management-ranking-wrapper"
              : "evaluation-management-ranking-wrapper-non-evaluated"
            : data.ranks.length
            ? "evaluation-management-ranking-wrapper"
            : [
                `evaluation-management-ranking-wrapper-non-evaluated`,
                `final-evaluation-management-ranking-wrapper-non-evaluated`,
              ].join(" ")
        }
      >
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
