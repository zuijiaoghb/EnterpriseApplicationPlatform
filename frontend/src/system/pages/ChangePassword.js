import React, { useState, useCallback } from 'react';
import { Form, Input, Button, message } from 'antd';
import api from '../../api';
import './ChangePassword.css'; // 使用新的样式文件


// 密码验证规则常量
const PASSWORD_RULES = {
  minLength: 8,
  regex: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
  message: '密码必须至少8位且包含大小写字母和数字'
};

const ChangePassword = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

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
            }, (
              { form }
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
            />
          </Form.Item>
          <Form.Item
            name="confirmPassword"
            label="确认新密码"
            dependencies={['newPassword']}
            rules={[{ 
              required: true,
              message: '请确认新密码'
            }, {
              validator: (_, value, callback) => {
                const newPassword = form.getFieldValue('newPassword');
                // 调试信息
                //console.log('新密码:', newPassword);
                //console.log('确认密码:', value);
                //console.log('密码是否一致:', newPassword === value);
                if (!value) {
                  callback(new Error('请确认新密码'));
                } else if (newPassword !== value) {
                  callback(new Error('两次输入的密码不一致'));
                } else {
                  callback();
                }
              }
            }]}
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