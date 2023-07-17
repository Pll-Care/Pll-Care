import ChartBox from "./ChartBox";
import ProjectBox from "./ProjectBox";

const Evaluate = () => {
  return (
    <div>
      <div className="profile_introduce_titleBox">
        <h1>평가 관리</h1>
      </div>
      <div className="evaluate">
        <ChartBox />
        <ProjectBox />
      </div>
    </div>
  );
};

export default Evaluate;
