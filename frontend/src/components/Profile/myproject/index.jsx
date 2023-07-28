import { useEffect, useState } from "react";
import ProjectItem from "./ProjectItem";
import Select from "../../common/Select";
import { recruitSelect } from "../../../utils/optionData";
import PaginationButton from "../../common/PaginationButton";
import { useQuery } from "react-query";
import { useProfile } from "../../../context/ProfileContext";
import { getPostProjectAPI } from "../../../lib/apis/profileApi";

const QUERY_KEY = "myproject";

const MyProject = () => {
  const [selecValue, setSelectValue] = useState("ONGOING");
  const [currentPageNumber, setCurrentPageNumber] = useState(1);

  const { memberId } = useProfile();

  const { data, refetch } = useQuery(
    [QUERY_KEY, memberId, currentPageNumber],
    () => getPostProjectAPI(memberId, selecValue, currentPageNumber)
  );

  const totalPages = data?.totalPages || 0;

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
    <div>
      <div className="profile_introduce_titleBox">
        <h1>내가 모집하는 프로젝트</h1>
      </div>
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
            {data?.content.map((project) => (
              <ProjectItem
                key={project.postId}
                projectId={project.projectId}
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
    </div>
  );
};

export default MyProject;
