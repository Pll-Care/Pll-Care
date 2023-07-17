const CHART_TITLE = {
  sincerity: "성실도",
  punctuality: "시간 엄수",
  jobPerformance: "업무 수행 능력",
  communication: "의사 소통",
};

const EvaluateChart = () => {
  return (
    <div className="evaluate_chart-warp">
      <div className="evaluate_chart">
        {dummyArr.map((item) => (
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

const dummy = {
  sincerity: 25,
  punctuality: 75,
  jobPerformance: 90,
  communication: 87,
};

const dummyArr = Object.keys(dummy).map((key) => {
  const title = CHART_TITLE[key];
  return { title, score: dummy[key] };
});
