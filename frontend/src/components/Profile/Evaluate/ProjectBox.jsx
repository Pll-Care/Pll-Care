import ProjectList from "./ProjectList";

const ProjectBox = () => {
  return (
    <section className="evaluate_Box" style={{ marginTop: "43px" }}>
      <div className="evaluate_Box_title">
        <h2>평가 종합 차트</h2>
      </div>
      <div>
        <ProjectList />
      </div>
    </section>
  );
};

export default ProjectBox;
