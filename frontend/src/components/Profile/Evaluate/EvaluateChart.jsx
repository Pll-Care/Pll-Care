import { useEffect, useState } from "react";
import { useProfile } from "../../../context/ProfileContext";
import { useQuery } from "react-query";
import { getEvaluationChartAPI } from "../../../lib/apis/profileApi";

const CHART_TITLE = {
  sincerity: "성실도",
  punctuality: "시간 엄수",
  jobPerformance: "업무 수행 능력",
  communication: "의사 소통",
};

const QUERY_KEY = "evaluateChart";

const EvaluateChart = () => {
  const { memberId } = useProfile();

  const [score, setScore] = useState([]);

  useQuery([QUERY_KEY, memberId], () => getEvaluationChartAPI(memberId), {
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
          <Bar key={item.title} title={item.title} score={item.score} />
        ))}
      </div>
      <div className="evaluate_chart_line"></div>
    </div>
  );
};

export default EvaluateChart;

const Bar = ({ title, score }) => {
  return (
    <div className="evaluate_chart_bars">
      <div className="evaluate_chart_bars_bar">
        <div
          className="evaluate_chart_bars_score"
          style={{ "--custom-value": `${score}%` }}
        ></div>
      </div>
      <div className="evaluate_chart_bars_title">{title}</div>
    </div>
  );
};

const extractScore = (data) => {
  return Object.keys(data).map((key) => {
    const title = CHART_TITLE[key];
    return { title, score: data[key] };
  });
};
