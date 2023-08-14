import { useEffect, useState } from "react";
import ProjectItem from "./ProjectItem";
import Select from "../../common/Select";
import { recruitSelect } from "../../../utils/optionData";
import PaginationButton from "../../common/PaginationButton";
import { useQuery } from "react-query";
import { useProfileClient } from "../../../context/Client/ProfileClientContext";

const QUERY_KEY = "my-project-Recruitment";

const RecruitmentProject = ({ memberId }) => {
  const [selecValue, setSelectValue] = useState("ONGOING");
  const [currentPageNumber, setCurrentPageNumber] = useState(1);
  const { getPostProjectAPI } = useProfileClient();

  const { data, refetch } = useQuery(
    [memberId, QUERY_KEY, currentPageNumber],
    () => getPostProjectAPI(selecValue, currentPageNumber)
  );

  const totalPages = data?.data?.totalPages || 0;

  const changeRecruit = (event) => {
    setSelectValue(event.target.value);
    setCurrentPageNumber(1);
  };

  const changePageNumber = (pageNumber) => {
    setCurrentPageNumber(pageNumber);
  };

  useEffect(() => {
    refetch();
  }, [selecValue, currentPageNumber, refetch]);

  return (
    <section>
      <div>
        <h1 className="profile_introduce_title">내가 모집하는 프로젝트</h1>
      </div>
      {data?.data?.content.length > 0 ? (
        <div className="myProject">
          <div className="myProject_selectContainer">
            <Select
              options={recruitSelect}
              onChange={changeRecruit}
              type={"small"}
            />
          </div>
          <div className="myProject_project">
            <ul>
              {data.data.content.map((project, idx) => (
                <ProjectItem
                  key={QUERY_KEY + "-" + idx}
                  postId={project.postId}
                  title={project.title}
                  description={project.description}
                />
              ))}
            </ul>
          </div>
          <PaginationButton
            changePageNumber={changePageNumber}
            totalPageNumber={totalPages}
            currentPageNumber={currentPageNumber}
          />
        </div>
      ) : (
        <div className="project-no-item">
          <p>모집한 프로젝트가 없습니다.</p>
        </div>
      )}
    </section>
  );
};

export default RecruitmentProject;

/*


*/
