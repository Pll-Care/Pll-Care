import project_default from "../../../assets/project-default-img.jpg";

const ProjectList = () => {
  return (
    <div className="evaluate_project">
      <ul className="evaluate_project_list">
        {dummy_project.map((item) => (
          <li className="evaluate_project_item">
            <div className="evaluate_project_item_user">
              <div className="evaluate_project_item_img">
                <img src={project_default} alt="프로젝트 이미지" />
              </div>
              <div className="evaluate_project_item_title">
                <span>{item.projectTitle}</span>
              </div>
            </div>
            <div className="evaluate_project_item_scores">
              <div>
                <div className="evaluate_project_item_score">
                  <span>성실도</span>
                  <span className="score-border">{item.score.sincerity}</span>
                </div>
                <div className="evaluate_project_item_score">
                  <span>엄무 수행 능력</span>
                  <span className="score-border">
                    {item.score.jobPerformance}
                  </span>
                </div>
              </div>
              <div>
                <div className="evaluate_project_item_score">
                  <span>시간 엄수</span>
                  <span className="score-border">{item.score.punctuality}</span>
                </div>
                <div className="evaluate_project_item_score">
                  <span>의사소통</span>
                  <span className="score-border">
                    {item.score.communication}
                  </span>
                </div>
              </div>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

const dummy_project = [
  {
    projectId: 0,
    projectTitle: "테스트 프로젝트 입니다.",
    score: {
      sincerity: 10,
      jobPerformance: 20,
      punctuality: 30,
      communication: 40,
    },
  },
  {
    projectId: 1,
    projectTitle: "테스트 프로젝트 입니다.",
    score: {
      sincerity: 80,
      jobPerformance: 60,
      punctuality: 40,
      communication: 46,
    },
  },
  {
    projectId: 2,
    projectTitle: "테스트 프로젝트 입니다.",
    score: {
      sincerity: 50,
      jobPerformance: 77,
      punctuality: 50,
      communication: 70,
    },
  },
  {
    projectId: 3,
    projectTitle: "테스트 프로젝트 입니다.",
    score: {
      sincerity: 30,
      jobPerformance: 40,
      punctuality: 50,
      communication: 20,
    },
  },
];

export default ProjectList;
