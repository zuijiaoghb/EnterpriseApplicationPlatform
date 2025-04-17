import React, { useState, useEffect } from 'react';
import { Layout, Menu, Dropdown, Avatar, message } from 'antd';
import { Outlet, useNavigate } from 'react-router-dom';
import { 
  LogoutOutlined, 
  UserOutlined,
  DashboardOutlined,
  ToolOutlined,
  SettingOutlined
} from '@ant-design/icons';
import api from '../api';
import './MainLayout.css';

const { Header, Content } = Layout;

const MainLayout = () => {
  const [currentUser, setCurrentUser] = useState(null);

  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  const [menuItems, setMenuItems] = useState([]);

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await api.get('/auth/info');
        setCurrentUser(response.data);
        console.log('User Info:', response.data);

        // 根据用户角色设置菜单项
        const baseItems = [
          {
            key: 'dashboard',
            icon: <DashboardOutlined />,
            label: '仪表盘',
          },
          {
            key: 'equipments',
            icon: <ToolOutlined />,
            label: '设备管理',
          }
        ];
        
        console.log('User Info roles:', response.data.roles?.some(role => role.authority === 'ROLE_ADMIN'));
        // 只有管理员才显示系统管理菜单
        if (response.data.roles?.some(role => role.authority === 'ROLE_ADMIN')) {
          baseItems.push({
            key: 'system',
            icon: <SettingOutlined />,
            label: '系统管理'
          });
        }
        
        setMenuItems(baseItems);
      } catch (error) {
        message.error('获取用户信息失败');
      }
    };
    
    fetchUserInfo();
  }, []);

  const userMenuItems = [
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout
    }
  ];

  return (
    <Layout className="layout">
      <Header className="main-header">
        <div className="logo-container">
          <div className="logo">企业应用平台</div>
        </div>
        <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={['dashboard']}
          items={menuItems}
          onClick={({ key }) => navigate(`/${key}`)}
          className="main-menu"
        />
        <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
          <div className="user-info">
            <Avatar icon={<UserOutlined />} />
            <span className="username">{currentUser?.username || '用户'}</span>
          </div>
        </Dropdown>
      </Header>
      <Content className="main-content">
        <div className="content-container">
          <Outlet />
        </div>
      </Content>
    </Layout>
  );
};

export default MainLayout;