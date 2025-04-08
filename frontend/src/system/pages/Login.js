import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import api from '../../api';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const response = await api.post('/auth/login', values, {
        withCredentials: true
      });

      // 调试所有可用头信息
      console.log('所有响应头:', Object.keys(response.headers));
      console.log('完整响应:', response);
      
      // 尝试获取各种可能的头名称
      const token = response.headers['authorization'] 
                || response.headers['Authorization']
                || response.headers['x-auth-token'];
      
      if (!token) {
        throw new Error('认证失败：未获取到有效token');
      }

      // 标准化token格式
      const normalizedToken = token.replace(/^Bearer\s+/i, '');
      localStorage.setItem('token', normalizedToken);
      
      // 设置全局token
      api.defaults.headers.common['Authorization'] = `Bearer ${normalizedToken}`;
      
      message.success('登录成功');
      navigate('/equipments');
    } catch (error) {
      console.error('完整错误详情:', {
        error: error.message,
        response: error.response,
        config: error.config
      });
      message.error(error.response?.data?.message || error.message || '网络请求异常');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: '0 auto' }}>
      <h2 style={{ textAlign: 'center', marginBottom: 24 }}>系统登录</h2>
      <Form onFinish={onFinish}>
        <Form.Item name="username" rules={[{ required: true }]}>
          <Input placeholder="用户名" />
        </Form.Item>
        <Form.Item name="password" rules={[{ required: true }]}>
          <Input.Password placeholder="密码" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading} block>
            登录
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default Login;