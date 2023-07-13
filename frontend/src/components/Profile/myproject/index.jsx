import { useState } from "react";
import ProjectItem from "./ProjectItem";
import Select from "../../common/Select";
import { recruitSelect, sortSelect } from "../../../utils/optionData";

const MyProject = () => {
  const [selecValue, setSelectValue] = useState({
    recruit: "ONGOING",
    sort: "NEWEST",
  });

  const changeRecruit = (event) => {
    setSelectValue((prev) => ({ ...prev, recruit: event.target.value }));
  };

  const changeSort = (event) => {
    setSelectValue((prev) => ({ ...prev, sort: event.target.value }));
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
          <Select options={sortSelect} onChange={changeSort} type="small" />
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
