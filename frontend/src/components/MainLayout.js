import React, { useState, useEffect } from 'react';
import { Layout, Menu, theme, Dropdown, Avatar, message } from 'antd';
import { Outlet, useNavigate } from 'react-router-dom';
import { LogoutOutlined, UserOutlined } from '@ant-design/icons';
import api from '../api';

const { Header, Content } = Layout;

const MainLayout = () => {
  const [currentUser, setCurrentUser] = useState(null);
  const {
    token: { colorBgContainer },
  } = theme.useToken();
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  const items = [
    {
      key: 'dashboard',
      label: '仪表盘',
    },
    {
      key: 'equipments',
      label: '设备管理',
    },
    {
      key: 'system',
      label: '系统管理',
    }
  ];

  const userMenuItems = [
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout
    }
  ];

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await api.get('/auth/info');
        setCurrentUser(response.data);
      } catch (error) {
        message.error('获取用户信息失败');
      }
    };
    
    fetchUserInfo();
  }, []);

  return (
    <Layout className="layout">
      <Header style={{ display: 'flex', alignItems: 'center' }}>
        <div className="logo" style={{ color: 'white', fontSize: '20px', marginRight: '24px' }}>
          企业应用平台
        </div>
        <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={['dashboard']}
          items={items}
          onClick={({ key }) => navigate(`/${key}`)}
          style={{ flex: 1, minWidth: 0 }}
        />
        <Dropdown menu={{ items: userMenuItems }}  placement="bottomRight">
          <div style={{ color: 'white', cursor: 'pointer' }}>
            <Avatar icon={<UserOutlined />} />
            <span style={{ marginLeft: 8 }}>{currentUser?.username || '用户'}</span>
          </div>
        </Dropdown>
      </Header>
      <Content style={{ padding: '24px 50px' }}>
        <div style={{ background: colorBgContainer, padding: 24, minHeight: 'calc(100vh - 64px - 48px)' }}>
          <Outlet />
        </div>
      </Content>
    </Layout>
  );
};

export default MainLayout;