import React, { useState, useEffect } from 'react';
import { 
  Table, Button, Modal, Form, Input, 
  message, Space, Tag, Card, Typography 
} from 'antd';
import api from '../../api';
import { 
  EyeOutlined, EyeInvisibleOutlined,
  PlusOutlined, EditOutlined, 
  DeleteOutlined, SafetyOutlined,
  LockOutlined, CheckCircleOutlined,
  CloseCircleOutlined
} from '@ant-design/icons';

const { Title } = Typography;

const ClientManagement = () => {
  const [clients, setClients] = useState([]);
  const [visible, setVisible] = useState(false);
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [currentClient, setCurrentClient] = useState(null);

  useEffect(() => {
    fetchClients();
  }, []);

  const fetchClients = async () => {
    const response = await api.get('/api/clients/all'); // 修改为获取所有客户端
    setClients(response.data);
  };

  const handleCreate = async () => {
    try {
      const values = await form.validateFields();
      // 移除客户端密钥字段，由后端生成
      delete values.clientSecret;
      const response = await api.post('/api/clients', values);
      setNewSecret(response.data.rawClientSecret);
      setSuccessModalVisible(true);
      setVisible(false);
      form.resetFields(); // 新增：重置表单字段
      fetchClients();
    } catch (error) {
      message.error('创建失败');
    }
  };

  const handleEdit = (client) => {
    form.resetFields(); // 新增：先重置表单
    setCurrentClient(client);
    form.setFieldsValue(client);
    setEditModalVisible(true);
  };
  
  const handleUpdate = async () => {
    try {
      const values = await form.validateFields();
      await api.put(`/api/clients/${currentClient.clientId}`, values);
      message.success('客户端更新成功');
      setEditModalVisible(false);
      form.resetFields(); // 新增：重置表单字段
      setCurrentClient(null); // 新增：清空当前客户端数据
      fetchClients();
    } catch (error) {
      message.error('更新失败');
    }
  };
  
  const [resetSecretModalVisible, setResetSecretModalVisible] = useState(false);
  const [resetClientId, setResetClientId] = useState(null);
  
  const handleResetSecret = (clientId) => {
    setResetClientId(clientId);
    setResetSecretModalVisible(true);
  };
  
  // 在state中添加
  const [successModalVisible, setSuccessModalVisible] = useState(false);
  const [newSecret, setNewSecret] = useState('');

  // 修改confirmResetSecret
  const confirmResetSecret = async () => {
    try {
      const response = await api.put(`/api/clients/${resetClientId}/reset-secret`);
      if (response.data && response.data.rawClientSecret) {
        setNewSecret(response.data.rawClientSecret);
        setSuccessModalVisible(true);
      }
      setResetSecretModalVisible(false);
      fetchClients();
    } catch (error) {
      message.error('重置失败: ' + error.message);
    }
  };

  const [disableModalVisible, setDisableModalVisible] = useState(false);
  const [disableClientId, setDisableClientId] = useState(null);
  
  const handleDisable = (clientId) => {
    setDisableClientId(clientId);
    setDisableModalVisible(true);
  };
  
  const confirmDisable = async () => {
    setLoading(true);
    try {
      await api.delete(`/api/clients/${disableClientId}`);
      message.success('客户端已禁用');
      setDisableModalVisible(false);
      fetchClients();
    } catch (error) {
      message.error('禁用失败');
      console.error('禁用客户端错误:', error);
    } finally {
      setLoading(false);
    }
  };

  const [enableModalVisible, setEnableModalVisible] = useState(false);
  const [enableClientId, setEnableClientId] = useState(null);
  
  const handleEnable = (clientId) => {
    setEnableClientId(clientId);
    setEnableModalVisible(true);
  };
  
  const confirmEnable = async () => {
    try {
      await api.put(`/api/clients/${enableClientId}/enable`);
      message.success('客户端已启用');
      setEnableModalVisible(false);
      fetchClients();
    } catch (error) {
      message.error('启用失败');
      console.error('启用客户端错误:', error);
    }
  };
  
  // columns中使用loading状态
  const columns = [
    {
      title: '客户端ID',
      dataIndex: 'clientId',
      key: 'clientId',
    },
    {
      title: '客户端名称',
      dataIndex: 'clientName',
      key: 'clientName',
    },
    {
      title: '客户端密钥',
      dataIndex: 'clientSecret',
      key: 'clientSecret',
      render: (secret) => (
        <Input.Password 
          value={secret} 
          visibilityToggle={{ visible: false }} // 默认不显示明文
          readOnly
          style={{ border: 'none', background: 'transparent' }}
          iconRender={(visible) => (
            <Button 
              type="text" 
              icon={visible ? <EyeOutlined /> : <EyeInvisibleOutlined />}
              onClick={(e) => e.stopPropagation()}
            />
          )}
        />
      )
    },
    {
      title: '权限范围',
      dataIndex: 'scopes',
      key: 'scopes',
    },
    {
      title: '状态',
      dataIndex: 'active',
      key: 'active',
      render: (active) => (
        <Tag color={active ? 'green' : 'red'}>
          {active ? '启用' : '禁用'}
        </Tag>
      )
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button onClick={() => handleEdit(record)}>编辑</Button>
          {record.active ? (
            <Button 
              danger 
              onClick={() => handleDisable(record.clientId)}
              loading={loading}
            >
              禁用
            </Button>
          ) : (
            <Button 
              type="primary"
              onClick={() => handleEnable(record.clientId)}
            >
              启用
            </Button>
          )}
          <Button onClick={() => handleResetSecret(record.clientId)}>
            重置密钥
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <Card 
      title={
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <SafetyOutlined style={{ fontSize: 24, marginRight: 12 }} />
          <Title level={4} style={{ margin: 0 }}>客户端管理</Title>
        </div>
      }
      bordered={false}
      extra={
        <Button 
          type="primary" 
          icon={<PlusOutlined />}
          onClick={() => setVisible(true)}
        >
          创建客户端
        </Button>
      }
    >
      <Table
        dataSource={clients}
        columns={[
          {
            title: <span><LockOutlined /> 客户端ID</span>,
            dataIndex: 'clientId',
            key: 'clientId',
            render: text => <Tag color="blue">{text}</Tag>
          },
          {
            title: '客户端名称',
            dataIndex: 'clientName',
            key: 'clientName',
            render: text => <Tag color="geekblue">{text}</Tag>
          },
          {
            title: '客户端密钥',
            dataIndex: 'clientSecret',
            key: 'clientSecret',
            render: (secret) => (
              <Input.Password 
                value={secret} 
                visibilityToggle={{ visible: false }}
                readOnly
                style={{ border: 'none', background: 'transparent' }}
                iconRender={(visible) => (
                  <Button 
                    type="text" 
                    icon={visible ? <EyeOutlined /> : <EyeInvisibleOutlined />}
                    onClick={(e) => e.stopPropagation()}
                  />
                )}
              />
            )
          },
          {
            title: '权限范围',
            dataIndex: 'scopes',
            key: 'scopes',
          },
          {
            title: '状态',
            dataIndex: 'active',
            key: 'active',
            render: (active) => (
              <Tag 
                color={active ? 'green' : 'red'}
                icon={active ? <CheckCircleOutlined /> : <CloseCircleOutlined />}
              >
                {active ? '启用' : '禁用'}
              </Tag>
            )
          },
          {
            title: '操作',
            key: 'action',
            render: (_, record) => (
              <Space>
                <Button 
                  icon={<EditOutlined />}
                  onClick={() => handleEdit(record)}
                >
                  编辑
                </Button>
                {record.active ? (
                  <Button 
                    danger 
                    icon={<DeleteOutlined />}
                    onClick={() => handleDisable(record.clientId)}
                    loading={loading}
                  >
                    禁用
                  </Button>
                ) : (
                  <Button 
                    type="primary"
                    onClick={() => handleEnable(record.clientId)}
                  >
                    启用
                  </Button>
                )}
                <Button onClick={() => handleResetSecret(record.clientId)}>
                  重置密钥
                </Button>
              </Space>
            ),
          },
        ]}
        rowKey="clientId"
        bordered
      />

      {/* 模态框部分保持原有功能，仅优化样式 */}
      <Modal
        title={
          <span>
            {currentClient ? <EditOutlined /> : <PlusOutlined />}
            {currentClient ? '编辑客户端' : '创建客户端'}
          </span>
        }
        open={visible}
        onOk={handleCreate}
        onCancel={() => {setVisible(false);form.resetFields();}}
        key="create-client-modal" // 新增：添加唯一key
      >
        <Form form={form} key={currentClient ? `edit-${currentClient.id}` : 'create'}>
          <Form.Item name="clientId" label="客户端ID" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="clientName" label="客户端名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          {/* 移除客户端密钥输入框 */}
          <Form.Item name="scopes" label="权限范围" rules={[{ required: true }]}>
            <Input placeholder="用逗号分隔，如：api.read,api.write" />
          </Form.Item>
        </Form>
      </Modal>
      {/* 添加成功提示Modal */}
      <Modal
        title="客户端创建成功"
        open={successModalVisible}
        onOk={() => setSuccessModalVisible(false)}
        onCancel={() => setSuccessModalVisible(false)}
        footer={[
          <Button key="ok" type="primary" onClick={() => setSuccessModalVisible(false)}>
            我已保存密钥
          </Button>
        ]}
      >
        <p>客户端密钥为: <b>{newSecret}</b></p>
        <p style={{color: 'red'}}>请妥善保存此密钥，离开此页面后将无法再次查看</p>
      </Modal>
      <Modal
        title="编辑客户端"
        open={editModalVisible}
        onOk={handleUpdate}
        onCancel={() => {setEditModalVisible(false);form.resetFields();}}
        key={`edit-client-${currentClient?.clientId || ''}`} // 新增：动态key
      >
        <Form form={form} layout="vertical">
          <Form.Item name="clientName" label="客户端名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="scopes" label="权限范围" rules={[{ required: true }]}>
            <Input placeholder="用逗号分隔，如：api.read,api.write" />
          </Form.Item>
        </Form>
      </Modal>
      <Modal
        title="确认重置客户端密钥"
        open={resetSecretModalVisible}
        onOk={confirmResetSecret}
        onCancel={() => setResetSecretModalVisible(false)}
        okText="确认"
        cancelText="取消"
      >
        <p>重置后原密钥将立即失效</p>
     </Modal>
     <Modal
      title="确认禁用客户端"
      open={disableModalVisible}
      onOk={confirmDisable}
      onCancel={() => setDisableModalVisible(false)}
      okText="确认"
      cancelText="取消"
    >
      <p>禁用后该客户端将无法获取新令牌</p>
    </Modal>    
    <Modal
      title="确认启用客户端"
      open={enableModalVisible}
      onOk={confirmEnable}
      onCancel={() => setEnableModalVisible(false)}
      okText="确认"
      cancelText="取消"
    >
      <p>确认要启用该客户端吗？</p>
    </Modal>
    <Modal
      title="密钥重置成功"
      open={successModalVisible}
      onOk={() => setSuccessModalVisible(false)}
      onCancel={() => setSuccessModalVisible(false)}
    >
      <p>新密钥为: <b>{newSecret}</b></p>
      <p style={{color: 'red'}}>请妥善保存此密钥，离开此页面后将无法再次查看</p>
    </Modal>
    
  </Card>    
  );
};

export default ClientManagement;