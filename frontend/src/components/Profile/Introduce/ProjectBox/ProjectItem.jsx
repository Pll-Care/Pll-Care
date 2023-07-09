const ProjectItem = ({
  title,
  description,
  startDate,
  endDate,
  techStack,
  projectId,
}) => {
  return (
    <li className="project_list_item">
      <div>{"2023"}</div>
      <div>
        <div className="project_list_item_title-date">
          <div className="fl">
            <div className="project_item_name">
              <span>프로젝트 명</span>
            </div>
            <div className="project_list_item_title">
              <span>{title}</span>
            </div>
          </div>
          {/*  */}
          <div className="fl">
            <div className="project_item_name">
              <span>진행 기간</span>
            </div>
            <div className="project_list_item_year">
              <span>{`${startDate}-${endDate}`}</span>
            </div>
          </div>
        </div>
        <div className="project_list_item_stack">
          <div className="project_item_name">
            <span>기술 스택</span>
          </div>
          <div className="project_list_item_stack-list">
            <ul>
              {techStack &&
                techStack.map((stack) => <li key={stack}>{stack}</li>)}
            </ul>
          </div>
        </div>
        <div className="project_list_item_description">
          <div className="project_item_name">
            <span>한 줄 설명</span>
          </div>
          <div>
            <span>{description}</span>
          </div>
        </div>
      </div>
    </li>
  );
};

export default ProjectItem;
/**
 * 
 *   {
    title: "프로젝트 이름",
    description: "프로젝트 이름을 작성한 프로젝트 입니다.",
    startDate: "2023-07-10",
    endDate: "2023-11-11",
    techStack: ["HTML", "CSS", "React", "JavaScript", "TypeScirpt"],
    projectId: 5,
  },
 */
