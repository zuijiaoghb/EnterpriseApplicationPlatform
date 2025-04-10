import React from 'react';
import { Card } from 'antd';

const SystemSettings = () => {
  return (
    <div>
      <Card title="系统设置" bordered={false}>
        <p>用户管理</p>
        <p>角色管理</p>
        <p>权限配置</p>
      </Card>
    </div>
  );
};

export default SystemSettings;