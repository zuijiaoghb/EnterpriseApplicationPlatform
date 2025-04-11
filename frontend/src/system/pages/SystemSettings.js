import React, { useState, useEffect } from 'react';
import { Tabs, Card, message } from 'antd';
import UserManagement from './UserManagement';
import RoleManagement from './RoleManagement';
import PermissionManagement from './PermissionManagement';
import api from '../../api';

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
    return <Card>您没有权限访问系统管理功能</Card>;
  }

  return (
    <Card>
      <Tabs defaultActiveKey="1">
        <TabPane tab="用户管理" key="1">
          <UserManagement />
        </TabPane>
        <TabPane tab="角色管理" key="2">
          <RoleManagement />
        </TabPane>
        <TabPane tab="权限配置" key="3">
          <PermissionManagement />
        </TabPane>
      </Tabs>
    </Card>
  );
};

export default SystemSettings;