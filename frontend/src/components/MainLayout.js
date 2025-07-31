import React, { useState, useEffect } from 'react';
import { Layout, Menu, Dropdown, Avatar, message } from 'antd';
import { Outlet, useNavigate } from 'react-router-dom';
import {
  LogoutOutlined,
  UserOutlined,
  DashboardOutlined,
  ToolOutlined,
  SettingOutlined,
  InboxOutlined,
  ShopOutlined
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
        // 先获取auth info
        const authResponse = await api.get('/auth/info');
        const username = authResponse.data.username;
        console.log('Auth Info Username:', username);

        // 再使用username调用新API获取用户详细信息
        const userResponse = await api.get(`/api/users/username/${username}`);
        setCurrentUser(userResponse.data);        

        // 根据用户角色设置菜单项
        const baseItems = [];
        const roles = authResponse.data.roles || [];
        const isAdmin = roles.some(role => role === 'ROLE_ADMIN');

        // 仪表盘菜单 - 仪表盘管理员或管理员可见
        if (isAdmin || roles.some(role => role === 'ROLE_YBPGL')) {
          baseItems.push({
            key: 'dashboard',
            icon: <DashboardOutlined />,
            label: '仪表盘',
          });
        }

        // 设备管理菜单 - 设备管理员或管理员可见
        if (isAdmin || roles.some(role => role === 'ROLE_SBGL')) {
          baseItems.push({
            key: 'equipments',
            icon: <ToolOutlined />,
            label: '设备管理',
          });
        }

        // 库存管理菜单 - 仓库管理员或管理员可见
        if (isAdmin || roles.some(role => role === 'ROLE_CKGLY')) {
          baseItems.push({
            key: 'inventorymanagement',
            icon: <InboxOutlined />,
            label: '库存管理'
          });
        }

        const hasSupplierRole = isAdmin || roles.some(role => role === 'ROLE_SUPPLIER');
        if (hasSupplierRole) {
          baseItems.push({
            key: 'supplierportal',
            icon: <ShopOutlined />,
            label: '供应商门户'
          });
        }
                
        // 只有管理员才显示系统管理菜单
        if (isAdmin) {
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
            <span className="username">{currentUser?.cnname || '用户'}</span>
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