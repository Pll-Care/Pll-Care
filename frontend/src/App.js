import { BrowserRouter, Routes, Route } from 'react-router-dom';

import './scss/fullcare.css';

import Home from './pages/Home';
import JWTToken from './components/JWTToken';
import Profile from './pages/Profile';
import Management from './pages/Management';
import Recruitment from './pages/Recruitment';

function App() {
  return (
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
  );
}

export default App;
