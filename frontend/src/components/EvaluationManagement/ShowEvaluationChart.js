import { BarChart, Bar, XAxis, YAxis, Tooltip } from "recharts";

const CustomTooltip = ({ active, payload, label }) => {
  if (active && payload && payload.length) {
    return (
      <div className="custom-tooltip">
        <p className="label">
          <h2>{`${label}`}</h2>
          {`아이디어 뱅크: ${payload[0].value}`}개<br />
          {`열정적인 참여자: ${payload[1].value}`}개<br />
          {`최고의 서포터: ${payload[2].value}`}개<br />
          {`탁월한 리더: ${payload[3].value}`}개
        </p>
      </div>
    );
  }

  return null;
};

const ShowEvaluationChart = ({ chartData }) => {
  function makeChartData(data) {
    const evaluations = {};

    data.forEach((memberData) => {
      memberData.evaluation.forEach((evaluationData) => {
        evaluations[evaluationData.evaluationBadge] = evaluationData.quantity;
      });
    });

    return evaluations;
  }

  function yAxisDomain(data) {
    let maxBadgeNum = 0;

    data.forEach((memberData) => {
      memberData.evaluation.forEach((evaluationData) => {
        if (evaluationData.quantity > maxBadgeNum) {
          maxBadgeNum = evaluationData.quantity;
        }
      });
    });

    return maxBadgeNum + (5 - (maxBadgeNum % 5));
  }

  const data = chartData.map((memberData) => {
    const evaluations = makeChartData([memberData]);

    return {
      name: memberData.name,
      ...evaluations,
    };
  });

  return (
    <div className="evaluation-management-chart-wrapper">
      <h1>배지 차트</h1>
      <div className="evaluation-management-show-evaluation-chart">
        <BarChart className="chart" width={610} height={380} data={data}>
          <Tooltip content={<CustomTooltip />} />
          <XAxis dataKey="name" tickSize={8} />
          <YAxis
            dataKey="averageEvaluation"
            domain={[0, yAxisDomain(chartData)]}
            tickCount={7}
          />
          <Bar dataKey="아이디어_뱅크" fill="#01E89E" />
          <Bar dataKey="열정적인_참여자" fill="#00AA72" />
          <Bar dataKey="최고의_서포터" fill="#D7D7D7" />
          <Bar dataKey="탁월한_리더" fill="#01E89E" />
        </BarChart>
      </div>
    </div>
  );
};

export default ShowEvaluationChart;
