import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { Layout } from 'antd';
import EquipmentList from './equipment/pages/EquipmentList';
import Login from './system/pages/Login';
import AuthGuard from './system/components/AuthGuard'; // 新增导入
import './App.css';

const { Content } = Layout;

function App() {
  return (
    <Router>
      <Layout className="layout">
        <Content style={{ padding: '50px' }}>
          <Routes>
            <Route path="/" element={
              <AuthGuard>
                <Login />
              </AuthGuard>
            } />
            <Route path="/equipments" element={
              <AuthGuard>
                <EquipmentList />
              </AuthGuard>
            } />
            <Route path="/login" element={<Login />} /> {/* 新增登录路由 */}
          </Routes>
        </Content>
      </Layout>
    </Router>
  );
}

export default App;