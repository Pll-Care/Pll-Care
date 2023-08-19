import { useState } from "react";
import { useProfile } from "../../../context/ProfileContext";
import { useQuery } from "react-query";
import { useProfileClient } from "../../../context/Client/ProfileClientContext";
import ChartBar from "./ChartBar";

const CHART_TITLE = {
  sincerity: "성실도",
  punctuality: "시간 엄수",
  jobPerformance: "업무 수행 능력",
  communication: "의사 소통",
};

const QUERY_KEY = "evaluateChart";

const EvaluateChart = () => {
  const [score, setScore] = useState([]);
  const { memberId } = useProfile();
  const { getEvaluationChartAPI } = useProfileClient();

  useQuery([memberId, QUERY_KEY], () => getEvaluationChartAPI(), {
    onSuccess: (res) => {
      const { data } = res;
      const extractData = extractScore(data.score);
      setScore([...extractData]);
    },
  });

  return (
    <div className="evaluate_chart-warp">
      <div className="evaluate_chart">
        {score.map((item) => (
          <ChartBar key={item.title} title={item.title} score={item.score} />
        ))}
        <div className="evaluate_chart_line"></div>
      </div>
    </div>
  );
};

export default EvaluateChart;

const extractScore = (data) => {
  return Object.keys(data).map((key) => {
    const title = CHART_TITLE[key];
    return { title, score: data[key] };
  });
};
