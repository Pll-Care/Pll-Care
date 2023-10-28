import { useEffect, useState } from "react";

const ChartBar = ({ title, score }) => {
  const [isMouseOver, setIsMouseOver] = useState(false);
  const [coordinate, setCoordinate] = useState({ x: 0, y: 0 });
  const [chartScore, setChartScore] = useState(0);
  const handleMouseMove = (event) => {
    setCoordinate({
      x: event.nativeEvent.offsetX + 10,
      y: event.nativeEvent.offsetY - (chartScore * 2 + 10),
    });
  };

  const onMouseOver = () => {
    setIsMouseOver(true);
  };
  const onMouseOut = () => {
    setIsMouseOver(false);
    setCoordinate({ x: 0, y: 0 });
  };

  useEffect(() => {
    const scorePercentage = Math.round((score / 5) * 100);
    setChartScore((_) => scorePercentage);
  }, [score]);

  return (
    <div className="evaluate_chart_bars">
      <div className="evaluate_chart_bars_bar">
        <div
          className="evaluate_chart_bars_score"
          style={{ "--custom-value": `${chartScore}%` }}
          onMouseMove={handleMouseMove}
          onMouseOver={onMouseOver}
          onMouseOut={onMouseOut}
        ></div>
        {isMouseOver && (
          <div
            className="evaluate_chart_bars_score_text"
            style={{
              transform: `translate(${coordinate.x}px, ${coordinate.y}px)`,
            }}
          >
            <p>{title}</p>
            <p>:</p>
            <p className="evaluate_chart_bars_score_title">{chartScore}</p>
          </div>
        )}
      </div>
      <div className="evaluate_chart_bars_title">{title}</div>
    </div>
  );
};

export default ChartBar;
