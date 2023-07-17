import EvaluateChart from "./EvaluateChart";

const ChartBox = () => {
  return (
    <section className="evaluate_Box">
      <div className="evaluate_Box_title">
        <h2>평가 종합 차트</h2>
      </div>
      <div>
        <EvaluateChart />
      </div>
    </section>
  );
};

export default ChartBox;
