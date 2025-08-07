import React, { useState, useCallback } from 'react';
import { View, Text, TextInput, Button, StyleSheet, Alert } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../navigation/types';
import api from '../api';

type ChangePasswordNavigationProp = StackNavigationProp<RootStackParamList, 'ChangePassword'>;

// 密码验证规则常量
const PASSWORD_RULES = {
  minLength: 8,
  regex: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
  message: '密码必须至少8位且包含大小写字母和数字'
};

const ChangePassword = () => {
  const navigation = useNavigation<ChangePasswordNavigationProp>();
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<{
    oldPassword?: string;
    newPassword?: string;
    confirmPassword?: string;
  }>({});

  // 验证表单
  const validateForm = (): boolean => {
    const newErrors: typeof errors = {};
    let isValid = true;

    // 验证旧密码
    if (!oldPassword) {
      newErrors.oldPassword = '请输入旧密码';
      isValid = false;
    }

    // 验证新密码
    if (!newPassword) {
      newErrors.newPassword = '请输入新密码';
      isValid = false;
    } else if (newPassword.length < PASSWORD_RULES.minLength) {
      newErrors.newPassword = `新密码长度不能少于${PASSWORD_RULES.minLength}个字符`;
      isValid = false;
    } else if (!PASSWORD_RULES.regex.test(newPassword)) {
      newErrors.newPassword = PASSWORD_RULES.message;
      isValid = false;
    }

    // 验证确认密码
    if (!confirmPassword) {
      newErrors.confirmPassword = '请确认新密码';
      isValid = false;
    } else if (confirmPassword !== newPassword) {
      newErrors.confirmPassword = '两次输入的密码不一致';
      isValid = false;
    }

    setErrors(newErrors);
    return isValid;
  };

  // 提交表单
  const handleSubmit = useCallback(async () => {
    if (!validateForm()) {
      return;
    }

    setLoading(true);
    // 定义API错误接口
    interface ApiError {
      response?: {
        data?: {
          message?: string;
        };
      };
    }

    try {
      await api.put('/api/users/change-password', {
        oldPassword,
        newPassword
      });

      Alert.alert('成功', '密码修改成功');
      // 重置表单
      setOldPassword('');
      setNewPassword('');
      setConfirmPassword('');
      setErrors({});
      // 返回上一页
      navigation.goBack();
    } catch (error: unknown) {
      // 将unknown类型断言为ApiError
      const apiError = error as ApiError;
      Alert.alert('失败', apiError.response?.data?.message || '密码修改失败');
    } finally {
      setLoading(false);
    }
  }, [oldPassword, newPassword, confirmPassword, navigation]);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>修改密码</Text>

      <View style={styles.formItem}>
        <Text style={styles.label}>旧密码</Text>
        <TextInput
          style={[styles.input, errors.oldPassword && styles.inputError]}
          placeholder="请输入旧密码"
          secureTextEntry
          value={oldPassword}
          onChangeText={setOldPassword}
          onBlur={() => {
            if (!oldPassword) {
              setErrors(prev => ({ ...prev, oldPassword: '请输入旧密码' }));
            } else {
              setErrors(prev => ({ ...prev, oldPassword: undefined }));
            }
          }}
        />
        {errors.oldPassword && <Text style={styles.errorText}>{errors.oldPassword}</Text>}
      </View>

      <View style={styles.formItem}>
        <Text style={styles.label}>新密码</Text>
        <TextInput
          style={[styles.input, errors.newPassword && styles.inputError]}
          placeholder="请输入新密码"
          secureTextEntry
          value={newPassword}
          onChangeText={setNewPassword}
          onBlur={() => {
            if (!newPassword) {
              setErrors(prev => ({ ...prev, newPassword: '请输入新密码' }));
            } else if (newPassword.length < PASSWORD_RULES.minLength) {
              setErrors(prev => ({ ...prev, newPassword: `新密码长度不能少于${PASSWORD_RULES.minLength}个字符` }));
            } else if (!PASSWORD_RULES.regex.test(newPassword)) {
              setErrors(prev => ({ ...prev, newPassword: PASSWORD_RULES.message }));
            } else {
              setErrors(prev => ({ ...prev, newPassword: undefined }));
            }
          }}
        />
        {errors.newPassword && <Text style={styles.errorText}>{errors.newPassword}</Text>}
        <Text style={styles.helperText}>密码必须至少8位且包含大小写字母和数字</Text>
      </View>

      <View style={styles.formItem}>
        <Text style={styles.label}>确认新密码</Text>
        <TextInput
          style={[styles.input, errors.confirmPassword && styles.inputError]}
          placeholder="请确认新密码"
          secureTextEntry
          value={confirmPassword}
          onChangeText={setConfirmPassword}
          onBlur={() => {
            if (!confirmPassword) {
              setErrors(prev => ({ ...prev, confirmPassword: '请确认新密码' }));
            } else if (confirmPassword !== newPassword) {
              setErrors(prev => ({ ...prev, confirmPassword: '两次输入的密码不一致' }));
            } else {
              setErrors(prev => ({ ...prev, confirmPassword: undefined }));
            }
          }}
        />
        {errors.confirmPassword && <Text style={styles.errorText}>{errors.confirmPassword}</Text>}
      </View>

      <Button
        title="确认修改"
        onPress={handleSubmit}
        disabled={loading}
        color="#1890ff"
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 30,
    textAlign: 'center',
    color: '#333',
  },
  formItem: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    marginBottom: 8,
    color: '#333',
  },
  input: {
    height: 40,
    borderColor: '#ddd',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 10,
    backgroundColor: '#fff',
  },
  inputError: {
    borderColor: '#f5222d',
  },
  errorText: {
    color: '#f5222d',
    fontSize: 12,
    marginTop: 4,
  },
  helperText: {
    color: '#999',
    fontSize: 12,
    marginTop: 4,
  },
});

export default ChangePassword;