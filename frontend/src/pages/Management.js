import { Outlet } from "react-router-dom";

// components
import { useRef, useState } from "react";
import MainHeader from "../components/MainHeader";
import MyProjectHeader from "../components/MyProjectHeader";
import ProjectList from "../components/ProjectList";

// const dummyProjectList = [
//   {
//     id: 0,
//     writer: "홍서현",
//     date: 1683162856400,
//     title: "제목1",
//     content: "안녕1",
//   },
//   {
//     id: 1,
//     writer: "김도연",
//     date: 1683062857459,
//     title: "제목2",
//     content: "안녕2",
//   },
//   {
//     id: 2,
//     writer: "이연제",
//     date: 1683292857459,
//     title: "제목3",
//     content: "안녕3",
//   },
//   {
//     id: 3,
//     writer: "조상욱",
//     date: 1682962857459,
//     title: "제목4",
//     content: "안녕4",
//   },
//   {
//     id: 4,
//     writer: "조상욱2",
//     date: 1682962857459,
//     title: "제목5",
//     content: "안녕4",
//   },
//   {
//     id: 5,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목6",
//     content: "안녕4",
//   },
//   {
//     id: 6,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목7",
//     content: "안녕4",
//   },
//   {
//     id: 7,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목8",
//     content: "안녕4",
//   },
//   {
//     id: 8,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목9",
//     content: "안녕4",
//   },
//   {
//     id: 9,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목10",
//     content: "안녕4",
//   },
//   {
//     id: 10,
//     writer: "홍서현",
//     date: 1683162856400,
//     title: "제목11",
//     content: "안녕1",
//   },
//   {
//     id: 11,
//     writer: "김도연",
//     date: 1683062857459,
//     title: "제목12",
//     content: "안녕2",
//   },
//   {
//     id: 12,
//     writer: "이연제",
//     date: 1683292857459,
//     title: "제목13",
//     content: "안녕3",
//   },
//   {
//     id: 13,
//     writer: "조상욱",
//     date: 1682962857459,
//     title: "제목14",
//     content: "안녕4",
//   },
//   {
//     id: 14,
//     writer: "조상욱2",
//     date: 1682962857459,
//     title: "제목15",
//     content: "안녕4",
//   },
//   {
//     id: 15,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목16",
//     content: "안녕4",
//   },
//   {
//     id: 16,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목17",
//     content: "안녕4",
//   },
//   {
//     id: 17,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목18",
//     content: "안녕4",
//   },
//   {
//     id: 18,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목19",
//     content: "안녕4",
//   },
//   {
//     id: 19,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목20",
//     content: "안녕4",
//   },
//   {
//     id: 20,
//     writer: "홍서현",
//     date: 1683162856400,
//     title: "제목1",
//     content: "안녕1",
//   },
//   {
//     id: 21,
//     writer: "김도연",
//     date: 1683062857459,
//     title: "제목2",
//     content: "안녕2",
//   },
//   {
//     id: 22,
//     writer: "이연제",
//     date: 1683292857459,
//     title: "제목3",
//     content: "안녕3",
//   },
//   {
//     id: 23,
//     writer: "조상욱",
//     date: 1682962857459,
//     title: "제목4",
//     content: "안녕4",
//   },
//   {
//     id: 24,
//     writer: "조상욱2",
//     date: 1682962857459,
//     title: "제목4",
//     content: "안녕4",
//   },
//   {
//     id: 25,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목4",
//     content: "안녕4",
//   },
//   {
//     id: 26,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목4",
//     content: "안녕4",
//   },
//   {
//     id: 27,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목4",
//     content: "안녕4",
//   },
//   {
//     id: 28,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목4",
//     content: "안녕4",
//   },
//   {
//     id: 29,
//     writer: "조상욱3",
//     date: 1682962857459,
//     title: "제목4",
//     content: "안녕4",
//   },
//   {
//     id: 30,
//     writer: "이재영",
//     date: 1682962857460,
//     title: "마지막 프로젝트",
//     content: "마지막 프로젝트 입니다!",
//   },
// ];

const Management = () => {
  const [data, setData] = useState([]);
  const projectId = useRef(0);

  // 새로운 프로젝트 작성
  const onCreate = (title, content) => {
    const date = new Date().getTime();

    const newProject = {
      id: projectId.current,
      title,
      content,
      date,
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
