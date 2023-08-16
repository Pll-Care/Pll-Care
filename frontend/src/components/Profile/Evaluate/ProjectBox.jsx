import ProjectList from "./ProjectList";

const ProjectBox = () => {
  return (
    <section className="evaluate_Box">
      <div className="evaluate_Box_title">
        <h2>받은 평가</h2>
      </div>
      <div>
        <ProjectList />
      </div>
    </section>
  );
};

export default ProjectBox;
