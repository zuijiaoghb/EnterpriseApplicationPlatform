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
            label: '仪表盘',
          },
          {
            key: 'equipments',
            label: '设备管理',
          }
        ];
        
        console.log('User Info roles:', response.data.roles?.some(role => role.authority === 'ROLE_ADMIN'));
        // 只有管理员才显示系统管理菜单
        if (response.data.roles?.some(role => role.authority === 'ROLE_ADMIN')) {
          baseItems.push({
            key: 'system',
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
      <Header style={{ display: 'flex', alignItems: 'center' }}>
        <div className="logo" style={{ color: 'white', fontSize: '20px', marginRight: '24px' }}>
          企业应用平台
        </div>
        <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={['dashboard']}
          items={menuItems}
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