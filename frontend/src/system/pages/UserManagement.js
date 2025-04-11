import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Modal, Form, Input, Select, message } from 'antd';
import api from '../../api';
import { useNavigate } from 'react-router-dom';

const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState(null);
  const [form] = Form.useForm();

  const navigate = useNavigate();
  const [hasAdminRole, setHasAdminRole] = useState(false);

  useEffect(() => {
    checkUserRole();
  }, []);

  const checkUserRole = async () => {
    try {
      const { data } = await api.get('/auth/check-role');
      setHasAdminRole(data.hasAdminRole);
      if (!data.hasAdminRole) {
        message.warning('您没有权限访问此功能');
        navigate('/'); // 跳转到首页而不是登录页
      }
    } catch (error) {
      message.error('获取用户权限失败');
      navigate('/');
    }
  };

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
        // 修改为正确的数据结构
        const userData = {
          username: values.username,
          password: values.password,
          email: values.email,
          roleIds: values.roleIds || []
        };
        await api.post('/api/users', userData);
        message.success('创建成功');
      }
      setVisible(false);
      form.resetFields();
      fetchUsers();
    } catch (error) {
      message.error('操作失败');
    }
  };

  const handleEditUser = (record) => {
    setCurrent(record);
    form.setFieldsValue({
      username: record.username,
      email: record.email,
      roleIds: record.roles?.map(role => role.id) || []
    });
    setVisible(true);
  };

  const handleDelete = async (id) => {
    await api.delete(`/api/users/${id}`);
    message.success('删除成功');
    fetchUsers();
  };

  // 添加邮箱校验函数
  const validateEmail = async (_, value) => {
    if (!value) {
      return Promise.reject('请输入邮箱');
    }
    
    try {
      const { data } = await api.get(`/api/users/check-email?email=${value}`);
      if (data.exists) {
        return Promise.reject('该邮箱已存在');
      }
      return Promise.resolve();
    } catch (error) {
      return Promise.reject('邮箱校验失败');
    }
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
                <Button onClick={() => handleEditUser(record)}>编辑</Button>
                <Button danger onClick={() => handleDelete(record.id)}>删除</Button>
              </Space>
            ),
          },
        ]}
      />
      <Modal
        title={current ? '编辑用户' : '新增用户'}
        open={visible}
        onOk={handleSubmit}
        onCancel={() => setVisible(false)}
      >
        <Form form={form} initialValues={current || {}}>
          <Form.Item name="username" label="用户名" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          
          {/* 密码输入栏位 - 新增用户或admin编辑时显示 */}
          {(hasAdminRole || !current) && (
            <Form.Item 
              name="password" 
              label="密码" 
              rules={[
                { required: !current, message: '请输入密码' },
                {
                  pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
                  message: '密码必须至少8位且包含大小写字母和数字'
                }
              ]}
            >
              <Input.Password placeholder={current ? '留空则不修改密码' : ''} />
            </Form.Item>
          )}
                    
          <Form.Item 
            name="email" 
            label="邮箱" 
            rules={[
              { required: true, message: '请输入邮箱' },
              { type: 'email', message: '请输入有效的邮箱地址' },
              { validator: validateEmail }
            ]}
          >
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