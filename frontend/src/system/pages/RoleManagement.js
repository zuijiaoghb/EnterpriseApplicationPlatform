import React, { useState, useEffect } from 'react';
import { 
  Table, Button, Space, Modal, Form, Input, 
  message, Transfer, Card, Tag, Typography 
} from 'antd';
import { 
  PlusOutlined, EditOutlined, DeleteOutlined, 
  LockOutlined, TeamOutlined, SafetyOutlined 
} from '@ant-design/icons';
import api from '../../api';
import { checkPermission } from '../services/auth';

const { Title } = Typography;

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
    <Card 
      title={
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <SafetyOutlined style={{ fontSize: 24, marginRight: 12 }} />
          <Title level={4} style={{ margin: 0 }}>角色管理</Title>
        </div>
      }
      bordered={false}
      extra={
        <Button 
          type="primary" 
          icon={<PlusOutlined />}
          onClick={() => { 
            setCurrent(null); 
            form.resetFields();
            setVisible(true); 
          }}
        >
          新增角色
        </Button>
      }
    >
      <Table
        dataSource={roles}
        columns={[
          { 
            title: <span><TeamOutlined /> 角色名称</span>,
            dataIndex: 'name',
            render: (text) => <Tag color="blue">{text}</Tag>
          },
          { 
            title: <span><LockOutlined /> 角色编码</span>,
            dataIndex: 'code',
            render: (text) => <Tag color="geekblue">{text}</Tag>
          },
          { title: '描述', dataIndex: 'description' },
          {
            title: '操作',
            render: (_, record) => (
              <Space>
                <Button 
                  icon={<EditOutlined />} 
                  onClick={() => { 
                    setCurrent(record);
                    form.resetFields();
                    form.setFieldsValue(record);
                    setVisible(true);
                  }}
                >
                  编辑
                </Button>
                <Button 
                  danger 
                  icon={<DeleteOutlined />}
                  onClick={() => handleDelete(record.id)}
                >
                  删除
                </Button>
                <Button 
                  type="dashed"
                  onClick={() => showPermissionModal(record)} 
                  disabled={!checkPermission('role:assign-permission')}
                >
                  分配权限
                </Button>
              </Space>
            ),
          },
        ]}
        rowKey="id"
        bordered
      />

      {/* 角色编辑/新增模态框 */}
      <Modal
        title={
          <span>
            {current ? <EditOutlined /> : <PlusOutlined />}
            {current ? '编辑角色' : '新增角色'}
          </span>
        }
        open={visible}
        onOk={handleSubmit}
        onCancel={() => {
          setVisible(false);
          form.resetFields();
        }}
        width={600}
      >
        <Form form={form} key={current ? `edit-${current.id}` : 'create'}>
          <Form.Item 
            name="name" 
            label="角色名称" 
            rules={[{ required: true, message: '请输入角色名称' }]}
          >
            <Input placeholder="请输入角色名称" />
          </Form.Item>
          <Form.Item 
            name="code" 
            label="角色编码" 
            rules={[{ required: true, message: '请输入角色编码' }]}
          >
            <Input placeholder="请输入角色编码" />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input.TextArea rows={3} placeholder="请输入角色描述" />
          </Form.Item>
        </Form>
      </Modal>

      {/* 权限分配模态框 */}
      <Modal
        title={
          <span>
            <SafetyOutlined /> 分配权限 - {selectedRole?.name}
          </span>
        }
        open={permissionModalVisible}
        onOk={saveRolePermissions}
        onCancel={() => setPermissionModalVisible(false)}
        width={800}
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
          listStyle={{
            width: '45%',
            height: 400,
          }}
          operations={['添加权限', '移除权限']}
          titles={['所有权限', '已分配权限']}
        />
      </Modal>
    </Card>
  );
};

export default RoleManagement;