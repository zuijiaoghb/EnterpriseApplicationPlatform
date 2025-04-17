import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Modal, Form, Input, message, Transfer } from 'antd';
import api from '../../api';
import { checkPermission } from '../services/auth';

const RoleManagement = () => {
  const [roles, setRoles] = useState([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState(null);
  const [form] = Form.useForm();
  const [permissions, setPermissions] = useState([]);
  const [permissionModalVisible, setPermissionModalVisible] = useState(false);
  const [selectedRole, setSelectedRole] = useState(null);
  const [targetKeys, setTargetKeys] = useState([]);

  useEffect(() => {
    fetchRoles();
    fetchAllPermissions(); // 添加这行调用
  }, []);

  const fetchRoles = async () => {
    try {
      const { data } = await api.get('/api/roles');
      setRoles(data);
    } catch (error) {
      message.error('获取角色列表失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      if (current) {
        await api.put(`/api/roles/${current.id}`, values);
        message.success('角色更新成功');
      } else {
        await api.post('/api/roles', values);
        message.success('角色创建成功');
      }
      setVisible(false);
      fetchRoles();
    } catch (error) {
      message.error('操作失败');
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/api/roles/${id}`);
      message.success('角色删除成功');
      fetchRoles();
    } catch (error) {
      message.error('删除失败');
    }
  };

  // 获取所有权限
  const fetchAllPermissions = async () => {
    const { data } = await api.get('/api/permissions');
    setPermissions(data);
  };

  // 打开权限分配模态框
  const showPermissionModal = async (role) => {
    setSelectedRole(role);
    const { data } = await api.get(`/api/roles/${role.id}/permissions`);
    setTargetKeys(data.map(p => p.id));
    setPermissionModalVisible(true);
  };

  // 保存权限分配
  const saveRolePermissions = async () => {
    await api.put(`/api/roles/${selectedRole.id}/permissions`, targetKeys);
    message.success('权限分配成功');
    setPermissionModalVisible(false);
  };  

  return (
    <div>
      <Button type="primary" onClick={() => { 
        setCurrent(null); 
        form.resetFields(); // 新增时重置表单
        setVisible(true); 
      }}>
        新增角色
      </Button>
      <Table
        dataSource={roles}
        columns={[
          { title: '角色名称', dataIndex: 'name' },
          { title: '角色编码', dataIndex: 'code' },
          { title: '描述', dataIndex: 'description' },
          {
            title: '操作',
            render: (_, record) => (
              <Space>                
                <Button onClick={() => { 
                  setCurrent(record);
                  form.resetFields(); // 先重置表单
                  form.setFieldsValue(record); // 再设置表单值
                  setVisible(true);
                }}>
                  编辑
                </Button>
                <Button danger onClick={() => handleDelete(record.id)}>删除</Button>
                <Button onClick={() => showPermissionModal(record)} disabled={!checkPermission('role:assign-permission')} >分配权限</Button>
              </Space>
            ),
          },
        ]}
        rowKey="id"
      />
      <Modal
        title={current ? '编辑角色' : '新增角色'}
        open={visible}
        onOk={handleSubmit}
        onCancel={() => {
          setVisible(false);
          form.resetFields(); // 取消时重置表单
        }}
      >
        <Form form={form}  key={current ? `edit-${current.id}` : 'create'} // 添加key强制重新渲染
        >
          <Form.Item name="name" label="角色名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="code" label="角色编码" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input.TextArea />
          </Form.Item>
        </Form>
      </Modal>
      <Modal
        title={`分配权限 - ${selectedRole?.name}`}
        open={permissionModalVisible}
        onOk={saveRolePermissions}
        onCancel={() => setPermissionModalVisible(false)}
      >
        <Transfer
          dataSource={permissions.map(p => ({
            key: p.id,
            title: p.name,
            description: p.description
          }))}
          targetKeys={targetKeys}
          onChange={setTargetKeys}
          render={item => item.title}
        />
      </Modal>
    </div>
  );
};

export default RoleManagement;