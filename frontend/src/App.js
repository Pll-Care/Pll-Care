import { BrowserRouter, Routes, Route } from "react-router-dom";
import React, { useEffect, useReducer } from "react";


import "./scss/fullcare.css";

import Home from "./pages/Home";
import JWTToken from "./components/JWTToken";
import Profile from "./pages/Profile";
import Management from "./pages/Management";
import Recruitment from "./pages/Recruitment";
import OverviewManagement from "./pages/OverviewManagement";
import MeetingRecordManagement from "./pages/MeetingRecordManagement";
import ScheduleManagement from "./pages/ScheduleManagement";
import EvaluationManagement from "./pages/EvaluationManagement";

export const AuthStateContext = React.createContext();
export const AuthDispatchContext = React.createContext();

const authStateReducer = (state, action) => {
  switch (action.type) {
    case "LOGIN": {
      const accessTokenData = localStorage.getItem("access_token");
      const refreshTokenData = localStorage.getItem("refresh_token");

      const newState = {
        ...state,
        accessToken: accessTokenData,
        refreshToken: refreshTokenData,
        isLoggedIn: true,
      };

      return newState;
    }
    case "LOGOUT": {
      localStorage.clear();

      const newState = {
        ...state,
        accessToken: "",
        refreshToken: "",
        isLoggedIn: false,
      };

      return newState;
    }
    default: {
      return {
        ...state,
        accessToken: "",
        refreshToken: "",
        isLoggedIn: false,
      };
    }
  }
};

function App() {
  const [authState, authStateDispatch] = useReducer(authStateReducer, {
    accessToken: "",
    refreshToken: "",
    isLoggedIn: false,
  });

  const onLogin = () => {
    authStateDispatch({
      type: "LOGIN",
    });
  };

  const onLogout = () => {
    authStateDispatch({
      type: "LOGOUT",
    });
  };

  useEffect(() => {
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');

    if (accessToken && refreshToken) {
      onLogin();
    }
  }, []);


  return (
    <AuthStateContext.Provider value={authState}>
      <AuthDispatchContext.Provider
        value={{
          onLogin,
          onLogout,
        }}
      >
        <BrowserRouter>
          <div className="App">
            <Routes>
              <Route path={"/"} element={<Home />} />
              <Route path={"/token"} element={<JWTToken />} />
              <Route path={"/profile"} element={<Profile />} />
              <Route path={"/recruitment"} element={<Recruitment />} />
              <Route path={"/management"} element={<Management />}>
                <Route path={"overview"} element={<OverviewManagement />} />
                <Route path={"meetingRecord"} element={<MeetingRecordManagement />} />
                <Route path={"schedule"} element={<ScheduleManagement />} />
                <Route path={"evaluation"} element={<EvaluationManagement />} />
              </Route>
            </Routes>
          </div>
        </BrowserRouter>
      </AuthDispatchContext.Provider>
    </AuthStateContext.Provider>
  );
}

export default App;
