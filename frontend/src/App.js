import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MainLayout from './components/MainLayout';
import Login from './system/pages/Login';
import EquipmentList from './equipment/pages/EquipmentList';
import SystemSettings from './system/pages/SystemSettings';
import Dashboard from './dashboard/pages/Dashboard';
import AuthGuard from './components/AuthGuard';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Login />} />
        <Route element={<AuthGuard><MainLayout /></AuthGuard>}>          
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/equipments" element={<EquipmentList />} />
          <Route path="/system" element={<SystemSettings />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;