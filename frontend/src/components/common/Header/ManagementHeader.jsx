import { Link } from "react-router-dom";

import { useProjectDetail } from "../../../context/ProjectDetailContext";
import { useRouter } from "../../../hooks/useRouter";

export const headerMenu = (projectId) => {
  return [
    {
      id: 1,
      link: `/management/${projectId}/overview`,
      title: "오버뷰",
      value: "overview",
    },
    {
      id: 2,
      link: `/management/${projectId}/meetingRecord`,
      title: "회의록",
      value: "meetingRecord",
    },
    {
      id: 3,
      link: `/management/${projectId}/schedule`,
      title: "일정",
      value: "schedule",
    },
    {
      id: 4,
      link: `/management/${projectId}/evaluation`,
      title: "평가",
      value: "evaluation",
    },
    {
      id: 5,
      link: `/management/${projectId}/teamMember`,
      title: "팀 관리",
      value: "teamMember",
    },
    {
      id: 6,
      link: `/management/${projectId}/project`,
      title: "관리",
      value: "project",
    },
  ];
};

const ManagementHeader = () => {
  const { currentPath } = useRouter();

  const { isLeader, projectId } = useProjectDetail();

  const headerLinks = headerMenu(projectId);

  const comparePath = (value) => {
    if (currentPath === `/management/${projectId}/${value}`) {
      return true;
    }

    return false;
  };

  return (
    <header className="management-header">
      {headerLinks.map((menu) =>
        menu.value === "project" ? (
          isLeader && (
            <Link
              className={comparePath(menu.value) ? `header-link-on` : ""}
              to={menu.link}
              key={menu.id}
            >
              {menu.title}
            </Link>
          )
        ) : (
          <Link
            className={comparePath(menu.value) ? `header-link-on` : ""}
            to={menu.link}
            key={menu.id}
          >
            {menu.title}
          </Link>
        )
      )}
    </header>
  );
};

export default ManagementHeader;
