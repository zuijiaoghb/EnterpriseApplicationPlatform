import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MainLayout from './components/MainLayout';
import Login from './system/pages/Login';
import EquipmentList from './equipment/pages/EquipmentList';
import SystemSettings from './system/pages/SystemSettings';
import InventoryManagement from './inventorymanagement/pages/InventoryManagement';
import Dashboard from './dashboard/pages/Dashboard';
import AuthGuard from './components/AuthGuard';
import { message } from 'antd';

// 配置message的全局参数
message.config({
  top: 100,
  duration: 3,
  maxCount: 3,
});

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
          <Route path="/inventorymanagement" element={<InventoryManagement />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;