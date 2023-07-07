import React from "react";
import { createBrowserRouter } from "react-router-dom";

import Home from "./pages/Home";
import JWTToken from "./components/Login/JWTToken";
import Profile from "./pages/Profile";
import Management from "./pages/Management";
import ProjectDetailPage from "./pages/ProjectDetailPage";
import OverviewManagement from "./pages/OverviewManagement";
import MeetingRecordManagement from "./pages/MeetingRecordManagement";
import ScheduleManagement from "./pages/ScheduleManagement";
import EvaluationManagement from "./pages/EvaluationManagement";
import TeamMemberManagement from "./components/TeamMemberManagement/TeamMemberManagement";
import GeneralLayout from "./layout/GeneralLayout";
import Recruitment from "./pages/Recruitment";
import RecruitmentDetailPage from "./pages/RecruitmentDetail";

const routerData = [
  {
    id: 0,
    path: "/",
    element: <Home />,
    withLogin: false,
  },
  {
    id: 1,
    path: "/token",
    element: <JWTToken />,
    withLogin: false,
  },
  {
    id: 2,
    path: "/profile",
    element: <Profile />,
    withLogin: true,
  },
  {
    id: 3,
    path: "/management",
    element: <Management />,
    withLogin: true,
  },
  {
    id: 4,
    path: "/recruitment",
    element: <Recruitment />,
    withLogin: false,
  },
  {
    id: 5,
    path: "/recruitment/:id",
    element: <RecruitmentDetailPage />,
    withLogin: true,
  },
  {
    id: 6,
    path: "/management/:id",
    element: <ProjectDetailPage />,
    withLogin: true,
    children: [
      {
        path: "overview",
        element: <OverviewManagement />,
        withLogin: true,
      },
      {
        path: "meetingRecord",
        element: <MeetingRecordManagement />,
        withLogin: true,
      },
      {
        path: "schedule",
        element: <ScheduleManagement />,
        withLogin: true,
      },
      {
        path: "evaluation",
        element: <EvaluationManagement />,
        withLogin: true,
      },
      {
        path: "teamMember",
        element: <TeamMemberManagement />,
        withLogin: true,
      },
    ],
  },
];

export const routers = createBrowserRouter(
  routerData.map((router) => {
    if (router.children) {
      return {
        path: router.path,
        element: <GeneralLayout>{router.element}</GeneralLayout>,
        children: router.children,
      };
    } else {
      return {
        path: router.path,
        element: <GeneralLayout>{router.element}</GeneralLayout>,
      };
    }
  })
);
