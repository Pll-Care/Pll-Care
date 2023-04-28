import { BrowserRouter, Routes, Route } from 'react-router-dom';

import './scss/project-full-care.css';

import Home from './pages/Home';
import Login from './components/Login';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path={'/'} element={<Home />} />
          <Route path={`/login`} element={<Login />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
