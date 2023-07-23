import { useState } from "react";
import ProjectItem from "./ProjectItem";
import Select from "../../common/Select";
import { recruitSelect, sortSelect } from "../../../utils/optionData";
import PaginationButton from "../../common/PaginationButton";

const MyProject = () => {
  const [selecValue, setSelectValue] = useState("ONGOING");
  const [currentPageNumber, setCurrentPageNumber] = useState(1);

  const changeRecruit = (event) => {
    setSelectValue(event.target.value);
    setCurrentPageNumber(1);
  };

  const changePageNumber = (pageNumber) => {
    setCurrentPageNumber(pageNumber);
  };

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
            {dummy.map((project) => (
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
          totalPageNumber={111}
          currentPageNumber={currentPageNumber}
        />
      </div>
    </div>
  );
};

export default MyProject;

const dummy = [
  {
    postId: 0,
    title: "신규 프로젝트",
    description: "유구한 역사와 전통에 빛나는 우리 대한민국",
  },
  {
    postId: 1,
    title: "신규 프로젝트",
    description: "유구한 역사와 전통에 빛나는 우리 대한민국",
  },
  {
    postId: 2,
    title: "신규 프로젝트",
    description: "유구한 역사와 전통에 빛나는 우리 대한민국",
  },
  {
    postId: 3,
    title: "신규 프로젝트",
    description: "유구한 역사와 전통에 빛나는 우리 대한민국",
  },
  {
    postId: 4,
    title: "신규 프로젝트",
    description: "유구한 역사와 전통에 빛나는 우리 대한민국",
  },
];
