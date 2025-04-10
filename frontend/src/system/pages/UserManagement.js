import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Modal, Form, Input, Select, message } from 'antd';
import api from '../../api';

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState(null);
  const [form] = Form.useForm();

  useEffect(() => {
    fetchUsers();
    fetchRoles();
  }, []);

  const fetchUsers = async () => {
    const { data } = await api.get('/api/users');
    setUsers(data.content);
  };

  const fetchRoles = async () => {
    const { data } = await api.get('/api/roles');
    setRoles(data);
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      if (current) {
        await api.put(`/api/users/${current.id}`, values);
        message.success('更新成功');
      } else {
        await api.post('/api/users', values);
        message.success('创建成功');
      }
      setVisible(false);
      fetchUsers();
    } catch (error) {
      message.error('操作失败');
    }
  };

  const handleDelete = async (id) => {
    await api.delete(`/api/users/${id}`);
    message.success('删除成功');
    fetchUsers();
  };

  return (
    <div>
      <Button type="primary" onClick={() => { setCurrent(null); setVisible(true); }}>
        新增用户
      </Button>
      <Table
        dataSource={users}
        columns={[
          { title: '用户名', dataIndex: 'username' },
          { title: '邮箱', dataIndex: 'email' },
          { title: '状态', dataIndex: 'status' },
          {
            title: '操作',
            render: (_, record) => (
              <Space>
                <Button onClick={() => { setCurrent(record); setVisible(true); }}>编辑</Button>
                <Button danger onClick={() => handleDelete(record.id)}>删除</Button>
              </Space>
            ),
          },
        ]}
      />
      <Modal
        title={current ? '编辑用户' : '新增用户'}
        visible={visible}
        onOk={handleSubmit}
        onCancel={() => setVisible(false)}
      >
        <Form form={form} initialValues={current || {}}>
          <Form.Item name="username" label="用户名" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="email" label="邮箱" rules={[{ required: true, type: 'email' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="roleIds" label="角色">
            <Select mode="multiple">
              {roles.map(role => (
                <Select.Option key={role.id} value={role.id}>{role.name}</Select.Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default UserManagement;