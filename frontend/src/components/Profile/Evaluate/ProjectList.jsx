/* eslint-disable react-hooks/exhaustive-deps */
import { useQuery } from "react-query";
import project_default from "../../../assets/project-default-img.jpg";
import { useRouter } from "../../../hooks/useRouter";
import { useProfile } from "../../../context/ProfileContext";
import { getEvaluationProjectListAPI } from "../../../lib/apis/profileApi";
import PaginationButton from "../../common/PaginationButton";
import { useEffect, useState } from "react";

const QUERY_KEY = "evaluate-projectList";

const ProjectList = () => {
  const { currentPath, haveDataTo } = useRouter();
  const { memberId } = useProfile();

  const [page, setPage] = useState({ totlaPages: 0, currentPage: 1 });
  const [content, setContent] = useState([]);

  const { refetch } = useQuery(
    [QUERY_KEY, memberId],
    () => getEvaluationProjectListAPI(memberId, page.currentPage),
    {
      onSuccess: (res) => {
        const { data } = res;
        setContent([...data.content]);
        setPage((prev) => ({ ...prev, totlaPages: data.totalPages }));
      },
    }
  );

  useEffect(() => {
    refetch();
  }, [page.currentPage]);

  //title: string
  // projectId: number
  const clickProjectItem = (title, projectId) => {
    const data = { title, projectId };
    haveDataTo(currentPath + "/" + title, data);
  };

  const changePageNumber = (pageNumber) => {
    setPage((prev) => ({ ...prev, currentPage: pageNumber }));
  };

  return (
    <div className="evaluate_project">
      <ul className="evaluate_project_list">
        {content.map((item, idx) => (
          <li
            key={idx}
            className="evaluate_project_item"
            onClick={() => clickProjectItem(item.projectTitle, item.projectId)}
          >
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
      <PaginationButton
        totalPageNumber={page.totlaPages}
        currentPageNumber={page.currentPage}
        changePageNumber={changePageNumber}
      />
    </div>
  );
};

export default ProjectList;
