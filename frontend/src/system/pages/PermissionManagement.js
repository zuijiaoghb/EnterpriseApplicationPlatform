import React, { useState, useEffect } from 'react';
import { Table, Button, Space, Modal, Form, Input, Tree, message, Card, Select } from 'antd';
import api from '../../api';

const PermissionManagement = () => {
  const [permissions, setPermissions] = useState([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState(null);
  const [form] = Form.useForm();
  const [treeData, setTreeData] = useState([]);

  useEffect(() => {
    fetchPermissions();
  }, []);

  const fetchPermissions = async () => {
    try {
      const { data } = await api.get('/api/permissions/tree');
      console.log('Fetched permissions:', data); // 打印获取到的数据
      setPermissions(Array.isArray(data) ? data : []); // 确保是数组
      buildTreeData(Array.isArray(data) ? data : []); // 确保是数组
    } catch (error) {
      message.error('获取权限列表失败');
      setPermissions([]); // 出错时设为空数组
      setTreeData([]); // 出错时设为空数组
    }
  };
  
  const buildTreeData = (data) => {
      const buildNode = (node) => {
          if (!node) return null;
          console.log('Building node.children:', node.children);
          
          // 统一处理Set和Array类型的子节点
          const hasChildren = node.children && 
                            (node.children.size > 0 || node.children.length > 0);
          
          return {
              title: node.name,
              key: node.id,
              children: hasChildren
                  ? Array.from(node.children).map(child => buildNode(child))
                  : undefined
          };
      };
      
      const tree = data.map(item => buildNode(item)).filter(Boolean);
      console.log('Tree data built:', tree);
      setTreeData(tree);
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      if (current) {
        await api.put(`/api/permissions/${current.id}`, {
          ...values,
          parentId: values.parentId || null // 处理空值情况
        });
        message.success('权限更新成功');
      } else {
        await api.post('/api/permissions', {
          ...values,
          parentId: values.parentId || null // 处理空值情况
        });
        message.success('权限创建成功');
      }
      setVisible(false);
      fetchPermissions();
    } catch (error) {
      message.error('操作失败');
    }
  };

  // 添加handleDelete函数
  const handleDelete = async (id) => {
    try {
      await api.delete(`/api/permissions/${id}`);
      message.success('权限删除成功');
      fetchPermissions();
    } catch (error) {
      message.error('删除失败');
    }
  };

  return (
    <div>
      <Button type="primary" onClick={() => { 
        setCurrent(null); 
        form.resetFields(); // 重置表单
        setVisible(true); 
      }}>
        新增权限
      </Button>
      <Table
        dataSource={permissions}
        columns={[
          { title: '权限名称', dataIndex: 'name' },
          { title: '权限编码', dataIndex: 'code' },
          { title: '描述', dataIndex: 'description' },
          {
            title: '操作',
            render: (_, record) => (
              <Space>
                <Button onClick={() => { 
                  setCurrent(record);
                  form.resetFields(); // 重置表单
                  form.setFieldsValue(record); // 设置表单值 
                  setVisible(true); 
                }}>编辑</Button>
                <Button danger onClick={() => handleDelete(record.id)}>删除</Button>
              </Space>
            ),
          },
        ]}
        rowKey="id"
      />
      <Card title="权限树" style={{ marginTop: 16 }}>
        <Tree treeData={treeData} />
      </Card>
      <Modal
        title={current ? '编辑权限' : '新增权限'}
        open={visible}
        onOk={handleSubmit}
        onCancel={() => setVisible(false)}
      >        
        <Form form={form} key={current ? `edit-${current.id}` : 'create'}>
          <Form.Item name="name" label="权限名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="code" label="权限编码" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="parentId" label="父权限">          
              <Select 
                placeholder="选择父权限(不选则为顶级权限)"
                allowClear
              >
                {Array.isArray(permissions) && permissions
                  .filter(p => !p.parentId)
                  .map(p => (
                    <Select.Option key={p.id} value={p.id}>
                      {p.name}
                    </Select.Option>
                  ))}
              </Select>
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input.TextArea />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default PermissionManagement;