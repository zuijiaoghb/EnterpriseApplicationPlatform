import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, message } from 'antd';
import api from '../../api';

const ClientManagement = () => {
  const [clients, setClients] = useState([]);
  const [visible, setVisible] = useState(false);
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchClients();
  }, []);

  const fetchClients = async () => {
    const response = await api.get('/api/clients');
    setClients(response.data);
  };

  const handleCreate = async () => {
    try {
      const values = await form.validateFields();
      await api.post('/api/clients', values);
      message.success('客户端创建成功');
      setVisible(false);
      fetchClients();
    } catch (error) {
      message.error('创建失败');
    }
  };

  const handleDisable = (clientId) => {
    Modal.confirm({
      title: '确认禁用客户端?',
      content: '禁用后该客户端将无法获取新令牌',
      okText: '确认',
      cancelText: '取消',
      onOk: async () => {
        setLoading(true);
        try {
          await api.delete(`/api/clients/${clientId}`);
          message.success('客户端已禁用');
          fetchClients();
        } catch (error) {
          message.error('禁用失败');
        } finally {
          setLoading(false);
        }
      }
    });
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
      title: '权限范围',
      dataIndex: 'scopes',
      key: 'scopes',
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Button 
          danger 
          onClick={() => handleDisable(record.clientId)}
          loading={loading}
        >
          禁用
        </Button>
      ),
    },
  ];

  return (
    <div>
      <Button type="primary" onClick={() => setVisible(true)} style={{ marginBottom: 16 }}>
        创建客户端
      </Button>
      <Table dataSource={clients} columns={columns} rowKey="clientId" />

      <Modal
        title="创建新客户端"
        visible={visible}
        onOk={handleCreate}
        onCancel={() => setVisible(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="clientId" label="客户端ID" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="clientName" label="客户端名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="clientSecret" label="客户端密钥" rules={[{ required: true }]}>
            <Input.Password />
          </Form.Item>
          <Form.Item name="scopes" label="权限范围" rules={[{ required: true }]}>
            <Input placeholder="用逗号分隔，如：api.read,api.write" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default ClientManagement;