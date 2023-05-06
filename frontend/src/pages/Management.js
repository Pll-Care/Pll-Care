// components
import MainHeader from "../components/MainHeader";
import MyProjectHeader from "../components/MyProjectHeader";
import ProjectList from "../components/ProjectList";

const dummyProjectList = [
  {
    id: 0,
    writer: "홍서현",
    date: 1683162856400,
    title: "제목1",
    content: "안녕1",
  },
  {
    id: 1,
    writer: "김도연",
    date: 1683062857459,
    title: "제목2",
    content: "안녕2",
  },
  {
    id: 2,
    writer: "이연제",
    date: 1683292857459,
    title: "제목3",
    content: "안녕3",
  },
  {
    id: 3,
    writer: "조상욱",
    date: 1682962857459,
    title: "제목4",
    content: "안녕4",
  },
  {
    id: 4,
    writer: "조상욱2",
    date: 1682962857459,
    title: "제목4",
    content: "안녕4",
  },
  {
    id: 5,
    writer: "조상욱3",
    date: 1682962857459,
    title: "제목4",
    content: "안녕4",
  },
  {
    id: 6,
    writer: "조상욱3",
    date: 1682962857459,
    title: "제목4",
    content: "안녕4",
  },
  {
    id: 7,
    writer: "조상욱3",
    date: 1682962857459,
    title: "제목4",
    content: "안녕4",
  },
  {
    id: 8,
    writer: "조상욱3",
    date: 1682962857459,
    title: "제목4",
    content: "안녕4",
  },
  {
    id: 9,
    writer: "조상욱3",
    date: 1682962857459,
    title: "제목4",
    content: "안녕4",
  },
  {
    id: 10,
    writer: "홍서현",
    date: 1683162856400,
    title: "제목1",
    content: "안녕1",
  },
];

const Management = () => {
  return (
    <div>
      <MainHeader />
      <MyProjectHeader />
      <ProjectList projectList={dummyProjectList} />
    </div>
  );
};

export default Management;
