import { BarChart, Bar, XAxis, YAxis, Tooltip } from "recharts";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import { useState } from "react";

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

const settings = {
  dots: true,
  infinite: true,
  speed: 500,
  slidesToShow: 1,
  slidesToScroll: 1
};

const ShowEvaluationChart = ({ chartData }) => {
  const data = chartData.map((memberData) => {
    const evaluations = makeChartData([memberData]);

    return {
      name: memberData.name,
      ...evaluations,
    };
  });

  const [chartDataNum, setChartDataNum] = useState(data.length < 9 ? data.length : Math.ceil(data.length / 3));

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

  const chartIndexNum = () => {
    let indexNum = [];

    for (let i = 0; i < data.length / chartDataNum; i++) {
      indexNum.push(i);
    }

    return indexNum;
  };

  return (
    <div className="evaluation-management-chart-wrapper">
      <h1>배지 차트</h1>
      <div className="evaluation-management-show-evaluation-chart">
        <Slider className="slider" {...settings}>
          {chartIndexNum().map((i) => (
            <div className={`chart chart_${i}`} key={i}>
              <BarChart
                className="chart"
                width={560}
                height={320}
                data={data.slice(i * chartDataNum, (i + 1) * chartDataNum)}
              >
                <Tooltip content={<CustomTooltip />} />
                <XAxis dataKey="name" tickSize={8} />
                <YAxis
                  dataKey="averageEvaluation"
                  domain={[0, yAxisDomain(chartData)]}
                  
                />
                <Bar dataKey="아이디어_뱅크" fill="#01E89E" />
                <Bar dataKey="열정적인_참여자" fill="#00AA72" />
                <Bar dataKey="최고의_서포터" fill="#D7D7D7" />
                <Bar dataKey="탁월한_리더" fill="#01E89E" />
              </BarChart>
            </div>
          ))}
        </Slider>
      </div>
    </div>
  );
};

export default ShowEvaluationChart;
