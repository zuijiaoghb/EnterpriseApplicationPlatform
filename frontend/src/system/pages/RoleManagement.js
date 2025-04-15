import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Modal, Form, Input, message } from 'antd';
import api from '../../api';

const RoleManagement = () => {
  const [roles, setRoles] = useState([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState(null);
  const [form] = Form.useForm();

  useEffect(() => {
    fetchRoles();
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
              </Space>
            ),
          },
        ]}
        rowKey="id"
      />
      <Modal
        title={current ? '编辑角色' : '新增角色'}
        visible={visible}
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
    </div>
  );
};

export default RoleManagement;