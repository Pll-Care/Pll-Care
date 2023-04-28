import { BrowserRouter, Routes, Route } from 'react-router-dom';

import './scss/project-full-care.css';

import Home from './pages/Home';
import Login from './components/Login';
import KakaoLogin from './components/KakaoLogin';
import GoogleLogin from './components/GoogleLogin';
import NaverLogin from './components/NaverLogin';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path={'/'} element={<Home />} />
          <Route path={`/login`} element={<Login />}>
            <Route path={'kakao'} element={<KakaoLogin />} />
            <Route path={'google'} element={<GoogleLogin />} />
            <Route path={'naver'} element={<NaverLogin />} />
          </Route>
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
