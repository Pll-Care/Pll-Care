import { BrowserRouter, Routes, Route } from "react-router-dom";
import React, { useEffect } from "react";
import { useDispatch } from 'react-redux';

import "./scss/fullcare.css";

import Home from "./pages/Home";
import JWTToken from "./components/Login/JWTToken";
import Profile from "./pages/Profile";
import Management from "./pages/Management";
import Recruitment from "./pages/Recruitment";
import OverviewManagement from "./pages/OverviewManagement";
import MeetingRecordManagement from "./pages/MeetingRecordManagement";
import ScheduleManagement from "./pages/ScheduleManagement";
import EvaluationManagement from "./pages/EvaluationManagement";
import ProjectDetailPage from "./pages/ProjectDetailPage";
import TeamMemberManagement from "./components/TeamMemberManagement/TeamMemberManagement";

import { authActions } from "./redux/authSlice";
import MainHeader from "./components/shared/MainHeader";

const App = () => {
  const dispatch = useDispatch();


  useEffect(() => {
    const accessToken = localStorage.getItem("access_token");
    const refreshToken = localStorage.getItem("refresh_token");

    if (accessToken && refreshToken) {
      dispatch(authActions.login());
    }
  }, []);

  return (
    <BrowserRouter>
      <div className="App">
        <MainHeader />
        <Routes>
          <Route path={"/"} element={<Home />} />
          <Route path={"/token"} element={<JWTToken />} />
          <Route path={"/profile"} element={<Profile />} />
          <Route path={"/recruitment"} element={<Recruitment />} />
          <Route path={"/management"} element={<Management />} />
          <Route path={"/management/:id"} element={<ProjectDetailPage />}>
            <Route path={"overview"} element={<OverviewManagement />} />
            <Route path={"meetingRecord"} element={<MeetingRecordManagement />} />
            <Route path={"schedule"} element={<ScheduleManagement />} />
            <Route path={"evaluation"} element={<EvaluationManagement />} />
            <Route path={"teamMember"} element={<TeamMemberManagement />} />
          </Route>
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
