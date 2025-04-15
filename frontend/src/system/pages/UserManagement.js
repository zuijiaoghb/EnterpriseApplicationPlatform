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

  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10,
    total: 0,
  });
  
  useEffect(() => {
    fetchUsers();
  }, [pagination.current, pagination.pageSize]); // 添加分页参数依赖
  
  const fetchUsers = async () => {
    try {
      const { data } = await api.get('/api/users', {
        params: {
          page: pagination.current - 1,
          size: pagination.pageSize
        }
      });
      const processedUsers = data.content.map(user => ({
        ...user,
        password: '******'
      }));
      setUsers(processedUsers);
      setPagination({
        ...pagination,
        total: data.totalElements
      });
    } catch (error) {
      message.error('获取用户列表失败');
    }
  };

  const fetchRoles = async () => {
    const { data } = await api.get('/api/roles');
    setRoles(data);
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      if (current) {
        // 只提交修改的字段
        const updateData = {
          username: values.username,
          email: values.email,
          roleIds: values.roleIds || []
        };
        // 只有密码不为空且不等于原始密码时才更新密码
        if (values.password && values.password !== current.password) {
          updateData.password = values.password;
        }
        await api.put(`/api/users/${current.id}`, updateData);
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
      password: '', // 编辑时清空密码字段
      roleIds: record.roles?.map(role => role.id) || []
    });
    setVisible(true);
  };

  const handleDelete = async (id) => {
    await api.delete(`/api/users/${id}`);
    message.success('删除成功');
    fetchUsers();
  };

  // 修改邮箱校验函数
  const validateEmail = async (_, value) => {
    if (!value) {
      return Promise.reject('请输入邮箱');
    }
    
    // 编辑时且邮箱未修改则不校验
    if (current && current.email === value) {
      return Promise.resolve();
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

  // 添加用户名校验函数
  const validateUsername = async (_, value) => {
    if (!value) {
      return Promise.reject('请输入用户名');
    }

    // 编辑时且用户名未修改则不校验
    if (current && current.username === value) {
      return Promise.resolve();
    }
    
    try {
      const { data } = await api.get(`/api/users/check-username?username=${value}`);
      if (data.exists) {
        return Promise.reject('该用户名已存在');
      }
      return Promise.resolve();
    } catch (error) {
      return Promise.reject('用户名校验失败');
    }
  };  

  return (
    <div>
      <Button 
        type="primary" 
        onClick={() => { 
          setCurrent(null); 
          form.resetFields(); // 新增用户时重置表单
          setVisible(true); 
        }}
      >
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
        pagination={{
          ...pagination,
          showSizeChanger: true,
          onChange: (page, pageSize) => {
            setPagination({...pagination, current: page, pageSize});
          },
          onShowSizeChange: (current, size) => {
            setPagination({...pagination, current: 1, pageSize: size});
          }
        }}
        rowKey="id"
      />
      <Modal
        title={current ? '编辑用户' : '新增用户'}
        open={visible}
        onOk={handleSubmit}
        onCancel={() => {
          setVisible(false);
          form.resetFields();
        }}
      >
        <Form form={form} key={current ? `edit-${current.id}` : 'create'} // 添加key强制重新渲染
        >
            <Form.Item 
              name="username" 
              label="用户名" 
              rules={[
                { required: true, message: '请输入用户名' },
                { validator: validateUsername }
              ]}
            >
              <Input />
          </Form.Item>
          
          {/* 密码输入栏位 - 新增用户或admin编辑时显示 */}
          {(hasAdminRole || !current) && (
            <Form.Item 
              name="password" 
              label="密码" 
              rules={[
                !current && {  // 仅在新增时必填
                  required: true, 
                  message: '请输入密码' 
                },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    // 编辑时且密码未修改则不校验
                    if (current && value === current.password) {
                      return Promise.resolve();
                    }
                    // 新增或修改密码时校验复杂度
                    if (!value || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/.test(value)) {
                      return Promise.resolve();
                    }
                    return Promise.reject('密码必须至少8位且包含大小写字母和数字');
                  }
                })
              ].filter(Boolean)}
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