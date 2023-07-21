import { BarChart, Bar, XAxis, YAxis, Tooltip } from "recharts";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

import { useState } from "react";

const CustomTooltip = ({ isCompleted, active, payload, label }) => {
  if (active && payload && payload.length) {
    return (
      <div className="custom-tooltip">
        {isCompleted ? (
          <>
            <p className="label">
              {`${label}`} <br />
              {`성실도: ${payload[0].value}`}점<br />
              {`시간 엄수: ${payload[1].value}`}점<br />
              {`업무 수행 능력: ${payload[2].value}`}점<br />
              {`의사 소통: ${payload[3].value}`}점
            </p>
          </>
        ) : (
          <>
            <p className="label">
              {`${label}`} <br />
              {`아이디어 뱅크: ${payload[0].value}`}개<br />
              {`열정적인 참여자: ${payload[1].value}`}개<br />
              {`최고의 서포터: ${payload[2].value}`}개<br />
              {`탁월한 리더: ${payload[3].value}`}개
            </p>
          </>
        )}
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
    for (let evaluationData in memberData.evaluation) {
      if (memberData.evaluation[evaluationData] > maxBadgeNum) {
        maxBadgeNum = memberData.evaluation[evaluationData];
      }
    }
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

const ShowEvaluationChart = ({ chartData = null, isCompleted }) => {
  const data = chartData.map((memberData) => {
    const evaluation = memberData.evaluation[0];

    return isCompleted
      ? {
          name: memberData.name,
          communication: evaluation.communication,
          jobPerformance: evaluation.jobPerformance,
          punctuality: evaluation.punctuality,
          sincerity: evaluation.sincerity,
        }
      : {
          name: memberData.name,
          ideaBankBadge: memberData.evaluation["아이디어_뱅크"],
          bestSupporterBadge: memberData.evaluation["최고의_서포터"],
          goodLeaderBadge: memberData.evaluation["탁월한_리더"],
          enthusiasticParticipantBadge:
            memberData.evaluation["열정적인_참여자"],
        };
  });

  const [chartDataNum, setChartDataNum] = useState(
    data.length < 9 ? data.length : Math.ceil(data.length / 3)
  );

  return chartData && isCompleted ? (
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
                <Tooltip
                  content={<CustomTooltip isCompleted={isCompleted} />}
                />
                <XAxis dataKey="name" tickSize={8} />
                <YAxis
                  dataKey="evaluation"
                  domain={[0, evaluateBadgeMaxNumRounded(chartData)]}
                />
                <Bar dataKey="sincerity" fill="#01E89E" />
                <Bar dataKey="punctuality" fill="#00AA72" />
                <Bar dataKey="jobPerformance" fill="#D7D7D7" />
                <Bar dataKey="communication" fill="#01E89E" />
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
                  dataKey="evaluation"
                  domain={[0, evaluateBadgeMaxNumRounded(chartData)]}
                />
                <Bar dataKey="ideaBankBadge" fill="#01E89E" />
                <Bar dataKey="enthusiasticParticipantBadge" fill="#00AA72" />
                <Bar dataKey="bestSupporterBadge" fill="#D7D7D7" />
                <Bar dataKey="goodLeaderBadge" fill="#01E89E" />
              </BarChart>
            </div>
          ))}
        </Slider>
      </div>
    </div>
  );
};

export default ShowEvaluationChart;
