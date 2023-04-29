import { BrowserRouter, Routes, Route } from 'react-router-dom';

import './scss/project-full-care.css';

import Home from './pages/Home';
import Login from './components/Login';
import JWTToken from './components/JWTToken';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path={'/'} element={<Home />} />
          <Route path={'/login'} element={<Login />} />
          <Route path={'/token'} element={<JWTToken />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
