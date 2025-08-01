import React, { useState, useCallback } from 'react';
import { Form, Input, Button, message } from 'antd';
import api from '../../api';
import './ChangePassword.css'; // 使用新的样式文件

// 防抖函数 - 移到组件外部
const debounce = (func, delay) => {
  let timeout;
  return function(...args) {
    clearTimeout(timeout);
    timeout = setTimeout(() => func.apply(this, args), delay);
  };
};

// 密码验证规则常量
const PASSWORD_RULES = {
  minLength: 8,
  regex: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
  message: '密码必须至少8位且包含大小写字母和数字'
};

const ChangePassword = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [passwordStrength, setPasswordStrength] = useState('');
  const [strengthText, setStrengthText] = useState('');
  const [strengthTips, setStrengthTips] = useState('');

  // 密码强度检查 - 使用 useCallback 缓存
  const checkPasswordStrength = useCallback((password) => {
    if (!password || password.length < PASSWORD_RULES.minLength) {
      setPasswordStrength('');
      setStrengthText('');
      setStrengthTips('');
      return;
    }

    let score = 0;
    const tips = [];

    if (password.length >= 8) {
      score++;
      tips.push('✓ 长度符合要求');
    } else {
      tips.push('✗ 长度至少8位');
    }

    if (password.match(/[A-Z]/)) {
      score++;
      tips.push('✓ 包含大写字母');
    } else {
      tips.push('✗ 缺少大写字母');
    }

    if (password.match(/[a-z]/)) {
      score++;
      tips.push('✓ 包含小写字母');
    } else {
      tips.push('✗ 缺少小写字母');
    }

    if (password.match(/[0-9]/)) {
      score++;
      tips.push('✓ 包含数字');
    } else {
      tips.push('✗ 缺少数字');
    }

    if (password.match(/[^A-Za-z0-9]/)) {
      score++;
      tips.push('✓ 包含特殊字符');
    } else {
      tips.push('✗ 可以添加特殊字符提高安全性');
    }

    if (score <= 2) {
      setPasswordStrength('weak');
      setStrengthText('弱');
    } else if (score <= 4) {
      setPasswordStrength('medium');
      setStrengthText('中');
    } else {
      setPasswordStrength('strong');
      setStrengthText('强');
    }

    setStrengthTips(tips.join('\n'));
  });

  // 防抖处理的密码强度检查 - 正确依赖于 checkPasswordStrength
  const debouncedCheckPasswordStrength = useCallback(
    debounce(checkPasswordStrength, 300),
    [checkPasswordStrength]
  );

  // 表单提交处理 - 已经正确使用了 useCallback


  const handleSubmit = useCallback(async (values) => {
    setLoading(true);
    try {
      await api.put('/api/users/change-password', {
        oldPassword: values.oldPassword,
        newPassword: values.newPassword
      });
      message.success('密码修改成功', 3);
      // 成功后添加动画效果
      const formEl = document.querySelector('.change-password-form');
      if (formEl) {
        formEl.classList.add('success-animation');
        setTimeout(() => {
          formEl.classList.remove('success-animation');
        }, 1000);
      }
      form.resetFields();
    } catch (error) {
      message.error(error.response?.data?.message || '密码修改失败', 3);
    } finally {
      setLoading(false);
    }
  }, [form]);

  return (
    <div className="change-password-container">
      <div className="change-password-form">
        <h2 className="change-password-title">修改密码</h2>
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          scrollToFirstError
        >
          <Form.Item
            name="oldPassword"
            label="旧密码"
            rules={[{
              required: true,
              message: '请输入旧密码'
            }]}
          >
            <Input.Password
              placeholder="请输入旧密码"
            />
          </Form.Item>
          <Form.Item
            name="newPassword"
            label="新密码"
            rules={[{
              required: true,
              message: '请输入新密码'
            }, {
              min: PASSWORD_RULES.minLength,
              message: `新密码长度不能少于${PASSWORD_RULES.minLength}个字符`
            }, (
              { getFieldValue }
            ) => ({
              validator(_, value) {
                if (!value || PASSWORD_RULES.regex.test(value)) {
                  return Promise.resolve();
                }
                return Promise.reject(PASSWORD_RULES.message);
              }
            })]}
            hasFeedback
          >
            <Input.Password
              placeholder="请输入新密码"
              onChange={(e) => debouncedCheckPasswordStrength(e.target.value)}
            />
            {passwordStrength && (
              <div className="password-strength">
                <div className={`strength-bar strength-${passwordStrength}`}></div>
                <div className="strength-text">密码强度: {strengthText}</div>
                {strengthTips && (
                  <div className="strength-tips">
                    {strengthTips.split('\n').map((tip, index) => (
                      <div key={index} className="tip-item">{tip}</div>
                    ))}
                  </div>
                )}
              </div>
            )}
          </Form.Item>
          <Form.Item
            name="confirmPassword"
            label="确认新密码"
            dependencies={['newPassword']}
            rules={[{
              required: true,
              message: '请确认新密码'
            }, (
              { getFieldValue }
            ) => ({
              validator(_, value) {
                const newPassword = getFieldValue('newPassword');
                if (!value) {
                  return Promise.reject(new Error('请确认新密码'));
                }
                if (newPassword !== value) {
                  return Promise.reject(new Error('两次输入的密码不一致'));
                }
                return Promise.resolve();
              }
            })]}
            hasFeedback
          >
            <Input.Password
              placeholder="请确认新密码"
            />
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              block
              style={{ marginTop: '10px' }}
            >
              确认修改
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default ChangePassword;