import React, { useState, useEffect } from 'react';
import { Card, Tree, Button, message } from 'antd';
import api from '../../api';

const PermissionManagement = () => {
  const [permissions, setPermissions] = useState([]);
  const [roles, setRoles] = useState([]);
  const [selectedRole, setSelectedRole] = useState(null);
  const [checkedKeys, setCheckedKeys] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [rolesRes, permissionsRes] = await Promise.all([
        api.get('/api/roles'),
        api.get('/api/permissions')
      ]);
      setRoles(rolesRes.data);
      setPermissions(permissionsRes.data);
    } catch (error) {
      message.error('获取数据失败');
    }
  };

  const handleRoleSelect = (role) => {
    setSelectedRole(role);
    setCheckedKeys(role.permissions || []);
  };

  const handleSave = async () => {
    try {
      await api.put(`/api/roles/${selectedRole.id}/permissions`, {
        permissionIds: checkedKeys
      });
      message.success('权限更新成功');
    } catch (error) {
      message.error('更新失败');
    }
  };

  return (
    <Card title="权限管理">
      <div style={{ display: 'flex', gap: 16 }}>
        <div style={{ width: 200 }}>
          <h3>角色列表</h3>
          {roles.map(role => (
            <div 
              key={role.id}
              onClick={() => handleRoleSelect(role)}
              style={{
                padding: 8,
                cursor: 'pointer',
                background: selectedRole?.id === role.id ? '#f0f0f0' : 'transparent'
              }}
            >
              {role.name}
            </div>
          ))}
        </div>
        <div style={{ flex: 1 }}>
          {selectedRole && (
            <>
              <h3>为角色 {selectedRole.name} 分配权限</h3>
              <Tree
                checkable
                checkedKeys={checkedKeys}
                onCheck={setCheckedKeys}
                treeData={permissions}
                fieldNames={{ title: 'name', key: 'id' }}
              />
              <Button 
                type="primary" 
                onClick={handleSave}
                style={{ marginTop: 16 }}
              >
                保存权限
              </Button>
            </>
          )}
        </div>
      </div>
    </Card>
  );
};

export default PermissionManagement;