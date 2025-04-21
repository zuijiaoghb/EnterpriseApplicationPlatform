import React, { useState, useEffect } from 'react';
import { Tabs, Card, message } from 'antd';
import { 
  UserOutlined, 
  TeamOutlined, 
  LockOutlined,
  SettingOutlined 
} from '@ant-design/icons';
import UserManagement from './UserManagement';
import RoleManagement from './RoleManagement';
import PermissionManagement from './PermissionManagement';
import ClientManagement from './ClientManagement'; // 导入新的组件
import api from '../../api';
import './SystemSettings.css'; // 新增样式文件

const { TabPane } = Tabs;

const SystemSettings = () => {
  const [hasAdminRole, setHasAdminRole] = useState(false);

  useEffect(() => {
    checkUserRole();
  }, []);

  const checkUserRole = async () => {
    try {
      const { data } = await api.get('/auth/check-role');
      setHasAdminRole(data.hasAdminRole);
    } catch (error) {
      message.error('获取用户权限失败');
    }
  };

  if (!hasAdminRole) {
    return (
      <Card className="no-permission-card">
        <div className="no-permission-content">
          <SettingOutlined style={{ fontSize: '48px', color: '#1890ff' }} />
          <h2>您没有权限访问系统管理功能</h2>
          <p>请联系系统管理员获取权限</p>
        </div>
      </Card>
    );
  }

  return (
    <Card className="system-settings-card">
      <Tabs 
        defaultActiveKey="1" 
        tabPosition="left"
        className="system-tabs"
      >
        <TabPane 
          tab={
            <span>
              <UserOutlined />
              用户管理
            </span>
          } 
          key="1"
        >
          <UserManagement />
        </TabPane>
        <TabPane 
          tab={
            <span>
              <TeamOutlined />
              角色管理
            </span>
          } 
          key="2"
        >
          <RoleManagement />
        </TabPane>
        <TabPane 
          tab={
            <span>
              <LockOutlined />
              权限配置
            </span>
          } 
          key="3"
        >
          <PermissionManagement />
        </TabPane>
        <TabPane 
          tab={
            <span>
              <LockOutlined />
              客户端管理
            </span>
          } 
          key="4"
        >
          <ClientManagement />
        </TabPane>
      </Tabs>
    </Card>
  );
};

export default SystemSettings;