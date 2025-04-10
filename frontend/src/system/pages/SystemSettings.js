import React, { useState } from 'react';
import { Tabs, Card } from 'antd';
import UserManagement from './UserManagement';
import RoleManagement from './RoleManagement';
import PermissionManagement from './PermissionManagement';

const { TabPane } = Tabs;

const SystemSettings = () => {
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