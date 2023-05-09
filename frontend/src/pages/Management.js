import { useRef, useState } from "react";
import { Outlet } from "react-router-dom";

// components
import MainHeader from "../components/MainHeader";
import MyProjectHeader from "../components/MyProjectHeader";
import ProjectList from "../components/ProjectList";

const Management = () => {
  const [data, setData] = useState([]);
  const projectId = useRef(0);

  // 새로운 프로젝트 작성
  const onCreate = (title, content) => {
    //const date = new Date().getTime();

    const newProject = {
      projectId: projectId.current,
      title,
      content,
    };
    projectId.current += 1;
    setData([newProject, ...data]);
  };

  return (
    <div>
      <MainHeader />
      <MyProjectHeader onCreate={onCreate} />
      <ProjectList projectList={data} />
    </div>
  );
};

export default Management;
