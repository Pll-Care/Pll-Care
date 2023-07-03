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
  slidesToScroll: 1,
};

const evaluateBadgeMaxNumRounded = (data) => {
  let maxBadgeNum = 0;

  data.forEach((memberData) => {
    memberData.evaluation.forEach((evaluationData) => {
      if (evaluationData.quantity > maxBadgeNum) {
        maxBadgeNum = evaluationData.quantity;
      }
    });
  });

  return maxBadgeNum + (5 - (maxBadgeNum % 5));
};

const chartIndices = (data, chunkSize) => {
  let indexArray = [];

  for (let i = 0; i < data.length / chunkSize; i++) {
    indexArray.push(i);
  }

  return indexArray;
};

function makeChartData(data) {
  const evaluations = {};

  data.forEach((memberData) => {
    memberData.evaluation.forEach((evaluationData) => {
      evaluations[evaluationData.evaluationBadge] = evaluationData.quantity;
    });
  });

  return evaluations;
}

const ShowEvaluationChart = ({ chartData = null, isCompleted }) => {
  const data = chartData.map((memberData) => {
    const evaluations = makeChartData([memberData]);

    return {
      name: memberData.name,
      ...evaluations,
    };
  });

  const [chartDataNum, setChartDataNum] = useState(
    data.length < 9 ? data.length : Math.ceil(data.length / 3)
  );

  return (chartData && isCompleted === "COMPLETE") ? (
    <div className="evaluation-management-chart-wrapper">
      <h1>최종 평가 차트</h1>
      <div className="evaluation-management-show-evaluation-chart">
        <Slider className="slider" {...settings}>
          {chartIndices(data, chartDataNum).map((i) => (
            <div key={i}>
              <BarChart
                className="chart"
                width={495}
                height={320}
                data={data.slice(i * chartDataNum, (i + 1) * chartDataNum)}
              >
                <Tooltip content={<CustomTooltip />} />
                <XAxis dataKey="name" tickSize={8} />
                <YAxis
                  dataKey="averageEvaluation"
                  domain={[0, evaluateBadgeMaxNumRounded(chartData)]}
                />
                <Bar dataKey="성실도" fill="#01E89E" />
                <Bar dataKey="시간 엄수" fill="#00AA72" />
                <Bar dataKey="업무 수행 능력" fill="#D7D7D7" />
                <Bar dataKey="의사 소통" fill="#01E89E" />
              </BarChart>
            </div>
          ))}
        </Slider>
      </div>
    </div>
  ) : (
    <div className="evaluation-management-chart-wrapper">
      <h1>배지 차트</h1>
      <div className="evaluation-management-show-evaluation-chart">
        <Slider className="slider" {...settings}>
          {chartIndices(data, chartDataNum).map((i) => (
            <div key={i}>
              <BarChart
                className="chart"
                width={495}
                height={320}
                data={data.slice(i * chartDataNum, (i + 1) * chartDataNum)}
              >
                <Tooltip content={<CustomTooltip />} />
                <XAxis dataKey="name" tickSize={8} />
                <YAxis
                  dataKey="averageEvaluation"
                  domain={[0, evaluateBadgeMaxNumRounded(chartData)]}
                />
                <Bar dataKey="아이디어 뱅크" fill="#01E89E" />
                <Bar dataKey="열정적인 참여자" fill="#00AA72" />
                <Bar dataKey="최고의 서포터" fill="#D7D7D7" />
                <Bar dataKey="탁월한 리더" fill="#01E89E" />
              </BarChart>
            </div>
          ))}
        </Slider>
      </div>
    </div>
  );
};

export default ShowEvaluationChart;
