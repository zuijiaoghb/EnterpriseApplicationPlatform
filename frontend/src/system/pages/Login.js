import React, { useState, useEffect } from 'react';
import { Form, Input, Button, message, Card, Typography, Row, Col } from 'antd';
import api from '../../api';
import { useNavigate } from 'react-router-dom';
import './Login.css'; // 新增样式文件
import { UserOutlined, LockOutlined, SafetyCertificateOutlined, ReloadOutlined } from '@ant-design/icons';
import bgImage from '../../assets/login-bg.jpg'; // 添加背景图片导入

const { Title } = Typography;


const Login = () => {
  const [loading, setLoading] = useState(false);
  const [loginError, setLoginError] = useState(false); // 新增状态
  const [captchaUrl, setCaptchaUrl] = useState('');
  const [captchaId, setCaptchaId] = useState('');

  const navigate = useNavigate();  

  // 获取验证码
  const fetchCaptcha = async () => {
    try {
      console.log('开始获取验证码...');
      const response = await api.get('/auth/captcha');
      console.log('验证码响应:', response.data);
      
      if (response.data && response.data.image) {
        setCaptchaId(response.data.captchaId);
        setCaptchaUrl(`data:image/png;base64,${response.data.image}`);
        console.log('验证码设置成功，ID:', response.data.captchaId);
      } else {
        console.error('验证码数据格式错误:', response.data);
        message.error('验证码数据格式错误');
      }
    } catch (error) {
      console.error('获取验证码失败:', error);
      console.error('错误详情:', {
        status: error.response?.status,
        data: error.response?.data,
        message: error.message
      });
      message.error('获取验证码失败: ' + (error.response?.data?.message || error.message));
    }
  };

  // 组件加载时获取验证码
  useEffect(() => {
    console.log('Login component mounted, fetching captcha...');
    fetchCaptcha();
  }, []);

  const onFinish = async (values) => {
    setLoading(true);
    setLoginError(false);
    
    // 清除之前残留的token和refreshToken
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    
    // 清除axios默认请求头中的Authorization
    delete api.defaults.headers.common['Authorization'];
    
    try {
      const loginData = {
        ...values,
        captchaId: captchaId,
        captcha: values.captcha
      };
      const response = await api.post('/auth/login', loginData, {
        withCredentials: true
      });
      
      console.log('response.status:',response.status);

      // 调试所有可用头信息
      console.log('所有响应头:', Object.keys(response.headers));
      console.log('完整响应:', response);
      
      // 从响应头获取标准OAuth2 Token
      const token = response.headers['authorization'] 
                || response.headers['Authorization']
                || response.headers['x-auth-token'];
      
      if (!token) {
        throw new Error('认证失败：未获取到有效token');
      }
      
      // 标准化token格式
      const normalizedToken = token.replace(/^Bearer\s+/i, '');
      localStorage.setItem('token', normalizedToken);
      
      // 存储用户信息
      if (response.data?.user) {
        localStorage.setItem('user', JSON.stringify({
          username: response.data.user.username,
          roles: response.data.user.roles
        }));
      }
      
      // 设置全局token
      api.defaults.headers.common['Authorization'] = `Bearer ${normalizedToken}`;
      
      message.success('登录成功');      
      
      const roles = response.data.user.roles.map(role => role.authority);

      // 根据角色判断跳转页面
      if (roles.includes('ROLE_ADMIN') || roles.includes('ROLE_YBPGL')) {
        navigate('/dashboard');
      } else if (roles.includes('ROLE_SBGL')) {
        navigate('/equipments');
      } else if (roles.includes('ROLE_CKGLY')) {
        navigate('/inventorymanagement');
      } else if (roles.includes('ROLE_SUPPLIER')) {
        navigate('/supplierportal');
      } else {
        // 默认跳转到dashboard
        navigate('/dashboard');
      }
    } catch (error) {
      console.error('登录错误:', error);
      
      // 处理验证码错误的特殊情况
      if (error.response?.data?.message?.includes('验证码')) {
        message.error(error.response.data.message || '验证码输入错误');
      } else {
        setLoginError(true);
        message.error(error.message || '登录失败');
      }
      
      // 登录失败后刷新验证码
      fetchCaptcha();
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

          <Row gutter={12} align="middle">
            <Col flex="auto">
              <Form.Item 
                name="captcha" 
                rules={[{ required: true, message: '请输入验证码' }]}
                style={{ marginBottom: 0 }}
              >
                <Input 
                  placeholder="验证码" 
                  size="large"
                  prefix={<SafetyCertificateOutlined className="input-icon" />}
                />
              </Form.Item>
            </Col>
            <Col flex="none">
              <div style={{ display: 'flex', alignItems: 'center', height: '40px' }}>
                {captchaUrl ? (
                  <img 
                    src={captchaUrl} 
                    alt="验证码" 
                    style={{ 
                      height: '40px', 
                      width: '120px',
                      cursor: 'pointer',
                      borderRadius: '6px',
                      border: '1px solid #d9d9d9',
                      objectFit: 'cover',
                      backgroundColor: '#fff'
                    }}
                    onClick={fetchCaptcha}
                    onError={(e) => {
                      console.error('验证码图片加载失败');
                      e.target.style.display = 'none';
                    }}
                  />
                ) : (
                  <div style={{ 
                    height: '40px', 
                    width: '120px', 
                    background: '#f5f5f5', 
                    border: '1px solid #d9d9d9',
                    borderRadius: '6px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontSize: 12,
                    color: '#999',
                    cursor: 'pointer'
                  }}
                  onClick={fetchCaptcha}
                  >
                    加载中...
                  </div>
                )}
                <Button 
                  type="text" 
                  icon={<ReloadOutlined />} 
                  onClick={fetchCaptcha}
                  style={{ marginLeft: 4, padding: '0 8px', height: '40px' }}
                  title="刷新验证码"
                />
              </div>
            </Col>
          </Row>
          
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