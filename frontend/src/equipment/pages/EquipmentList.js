import React, { useState, useEffect } from 'react';
import { Table, Button, Space, message } from 'antd';
import EquipmentForm from './EquipmentForm';
import api from '../../api';

const EquipmentList = () => {
  const [equipments, setEquipments] = useState([]);
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      window.location.href = '/login'; // 改为使用react-router的导航
      return;
    }
    fetchEquipments();
  }, []);

  const fetchEquipments = async () => {
    try {
      console.log('正在请求设备数据...');
      const { data } = await api.get('/api/equipments');
      console.log('获取到的数据:', data);
      setEquipments(data);
    } catch (error) {
      console.error('API请求失败:', error);
      message.error('获取设备列表失败');
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/api/equipments/${id}`);
      message.success('删除成功');
      fetchEquipments();
    } catch (error) {
      message.error('删除失败');
    }
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    { title: '名称', dataIndex: 'name', key: 'name' },
    { title: '型号', dataIndex: 'model', key: 'model' },
    { title: '状态', dataIndex: 'status', key: 'status' },
    { title: '二维码', dataIndex: 'qrCode', key: 'qrCode' },
    { title: '最后维护', dataIndex: 'lastMaintenance', key: 'lastMaintenance' },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <Button onClick={() => { setCurrent(record); setVisible(true); }}>编辑</Button>
          <Button danger onClick={() => handleDelete(record.id)}>删除</Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Button type="primary" onClick={() => { setCurrent(null); setVisible(true); }} style={{ marginBottom: 16 }}>
        新增设备
      </Button>
      <Table columns={columns} dataSource={equipments} rowKey="id" />
      <EquipmentForm 
        open={visible}  // 将visible改为open
        onCancel={() => setVisible(false)} 
        onSuccess={fetchEquipments}
        current={current}
      />
    </div>
  );
};

export default EquipmentList;