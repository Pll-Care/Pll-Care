import { BrowserRouter, Routes, Route } from 'react-router-dom';

import './scss/fullcare.css';

import Home from './pages/Home';
import JWTToken from './components/JWTToken';
import Profile from './pages/Profile';
import Management from './pages/Management';
import Recruitment from './pages/Recruitment';
import React, { useReducer } from 'react';

export const AuthStateContext = React.createContext();
export const AuthDispatchContext = React.createContext();

const authStateReducer = (state, action) => {
  switch (action.type) {
    case 'LOGIN': {
      const accessTokenData = localStorage.getItem('access_token');
      const refreshTokenData = localStorage.getItem('refresh_token');

      const newState = {
        ...state,
        accessToken: accessTokenData,
        refreshToken: refreshTokenData
      };

      console.log(newState);
      return newState;
    }
    case 'LOGOUT': {
      localStorage.clear();

      const newState = {
        ...state,
        accessToken: '',
        refreshToken: ''
      }

      console.log(newState);
      return newState;
    }
    default: {
      return {
        ...state,
        accessToken: '',
        refreshToken: ''
      };
    }
  }
};

function App() {
  const [authState, authStateDispatch] = useReducer(authStateReducer, {
      accessToken: '',
      refreshToken: ''
  });

  const onLogin = () => {
    authStateDispatch({
      type: 'LOGIN'
    })
  }
  
  const onLogout = () => {
    authStateDispatch({
      type: 'LOGOUT'
    })
  }

  return (
    <AuthStateContext.Provider value={authState}>
      <AuthDispatchContext.Provider value={{
        onLogin,
        onLogout
      }}>
        <BrowserRouter>
          <div className="App">
            <Routes>
              <Route path={'/'} element={<Home />} />
              <Route path={'/token'} element={<JWTToken />} />
              <Route path={'/profile'} element={<Profile />} />
              <Route path={'/management'} element={<Management />} />
              <Route path={'/recruitment'} element={<Recruitment />} />
            </Routes>
          </div>
        </BrowserRouter>
      </AuthDispatchContext.Provider>
    </AuthStateContext.Provider>
  );
}

export default App;
