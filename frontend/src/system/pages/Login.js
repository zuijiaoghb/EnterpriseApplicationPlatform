import React, { useState } from 'react';
import { Form, Input, Button, message, Card, Typography } from 'antd';
import api from '../../api';
import { useNavigate } from 'react-router-dom';
import './Login.css'; // 新增样式文件
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import bgImage from '../../assets/login-bg.jpg'; // 添加背景图片导入

const { Title } = Typography;


const Login = () => {
  const [loading, setLoading] = useState(false);
  const [loginError, setLoginError] = useState(false); // 新增状态

  const navigate = useNavigate();  

  const onFinish = async (values) => {
    setLoading(true);
    setLoginError(false); // 重置错误状态
    try {
      const response = await api.post('/auth/login', values, {
        withCredentials: true
      });
      
      console.log('response.status:',response.status);

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
    
      console.log('response.data.user:', response.data?.user);
      // 新增：确保存储完整的用户信息（包含roles）
      if (response.data?.user) {
        if (!response.data.user.roles) {
          // 如果返回的用户信息没有roles，则主动获取
          const userInfo = await api.get('/auth/info');
          localStorage.setItem('user', JSON.stringify({
            ...response.data.user,
            roles: userInfo.data.roles
          }));
        } else {
          localStorage.setItem('user', JSON.stringify(response.data.user));
        }
      } else {
        // 如果没有返回用户信息，则获取完整用户信息
        const userInfo = await api.get('/auth/info');
        localStorage.setItem('user', JSON.stringify(userInfo.data));
      }
      
      // 设置全局token
      api.defaults.headers.common['Authorization'] = `Bearer ${normalizedToken}`;
      
      message.success('登录成功');              
      
      navigate('/dashboard');
    } catch (error) {      
      console.error('完整错误详情:', {
        error: error.message,
        response: error.response,
        config: error.config
      });
     
    // 修复后的错误处理逻辑
    if (error.response) {
      console.log('error.response.status:', error.response.status);
      if (error.response.status === 401) {
        setLoginError(true);
        message.error('用户名或密码错误', 3);
      } else {
        message.error(error.response.data?.message || '网络请求异常', 3);
      }
    } else {
      message.error(error.response?.data?.message || error.message || '网络请求异常', 3);
    }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container" style={{ 
      backgroundImage: `url(${bgImage})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center'
    }}>
      <Card className="login-card" hoverable>
        <div className="login-header">
          <Title level={3} className="login-title">企业应用平台</Title>
          <div className="login-subtitle">欢迎登录</div>
        </div>
        
        <Form onFinish={onFinish} layout="vertical">
          {/* 添加错误提示 */}
          {loginError && (
            <div style={{ color: 'red', marginBottom: 16, textAlign: 'center' }}>
              用户名或密码错误
            </div>
          )}

          <Form.Item 
            name="username" 
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input 
              placeholder="用户名" 
              size="large"
              prefix={<UserOutlined className="input-icon" />}
            />
          </Form.Item>
          
          <Form.Item 
            name="password" 
            rules={[{ required: true, message: '请输入密码' }]}
          >
            <Input.Password 
              placeholder="密码" 
              size="large"
              prefix={<LockOutlined className="input-icon" />}
            />
          </Form.Item>
          
          <Form.Item>
            <Button 
              type="primary" 
              htmlType="submit" 
              loading={loading} 
              block
              size="large"
              className="login-button"
            >
              登 录
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default Login;