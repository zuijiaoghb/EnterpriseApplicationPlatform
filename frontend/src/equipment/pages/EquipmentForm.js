import React, { useEffect } from 'react';
import { Modal, Form, Input, Select, DatePicker, message } from 'antd';
import api from '../../api';

const { Option } = Select;

const EquipmentForm = ({ visible, onCancel, onSuccess, current }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (current) {
      form.setFieldsValue(current);
    } else {
      form.resetFields();
    }
  }, [current, form]);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      if (current) {
        await api.put(`/api/equipments/${current.id}`, values);
        message.success('更新成功');
      } else {
        await api.post('/api/equipments', values);
        message.success('创建成功');
      }
      onSuccess();
      onCancel();
    } catch (error) {
      message.error('操作失败');
    }
  };

  return (
    <Modal
      title={current ? '编辑设备' : '新增设备'}
      open={visible}  // 使用props中的visible参数
      onOk={handleSubmit}
      onCancel={onCancel}
      forceRender
    >
      <Form form={form} layout="vertical">
        <Form.Item name="name" label="设备名称" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="model" label="设备型号">
          <Input />
        </Form.Item>
        <Form.Item name="status" label="状态" rules={[{ required: true }]}>
          <Select>
            <Option value="RUNNING">运行中</Option>
            <Option value="MAINTENANCE">维护中</Option>
            <Option value="FAULT">故障</Option>
          </Select>
        </Form.Item>
        <Form.Item name="qrCode" label="二维码">
          <Input />
        </Form.Item>
        <Form.Item name="lastMaintenance" label="最后维护时间">
          <DatePicker showTime style={{ width: '100%' }} />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EquipmentForm;