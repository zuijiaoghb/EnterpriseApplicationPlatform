import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, ImageBackground } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useNavigation } from '@react-navigation/native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { LoginScreenNavigationProp } from '../navigation/types';
import api from '../api';
import Icon from 'react-native-vector-icons/AntDesign';
import axios from 'axios';
// 提取公共的 name 和 size 值，避免多次指定
const DEFAULT_ICON_NAME = "user";
const DEFAULT_ICON_SIZE = 20;
const UserOutlined = (props: React.ComponentProps<typeof Icon>) => <Icon {...props} name={DEFAULT_ICON_NAME} size={DEFAULT_ICON_SIZE} />;
// 提取公共的 name 和 size 值，避免多次指定
const LOCK_ICON_NAME = "lock";
const LOCK_ICON_SIZE = 20;
const LockOutlined = (props: React.ComponentProps<typeof Icon>) => <Icon {...props} name={LOCK_ICON_NAME} size={LOCK_ICON_SIZE} />;

interface LoginResponse {
  username: string;
  roles?: string[];
}

const Login = () => {
  const [loading, setLoading] = useState(false);
  const [loginError, setLoginError] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  
  type RootStackParamList = {
    Login: undefined;
    Dashboard: undefined;
    Main: undefined;
  };

  type LoginScreenNavigationProp = NativeStackNavigationProp<RootStackParamList, 'Login'>;

  const navigation = useNavigation<LoginScreenNavigationProp>();

  const handleLogin = async () => {
    setLoading(true);
    setLoginError('');
    try {
      const response = await api.post<LoginResponse>('/auth/login', {
        username,
        password
      }, {
        withCredentials: true
      });
      
      const token = response.headers['authorization'] 
                || response.headers['Authorization']
                || response.headers['x-auth-token'];
      
      if (!token) {
        throw new Error('认证失败：未获取到有效token');
      }
      
      const normalizedToken = token.replace(/^Bearer\s+/i, '');
      
      // 存储token和用户信息
      await AsyncStorage.setItem('token', normalizedToken);
      await AsyncStorage.setItem('user', JSON.stringify({
        username: response.data?.username,
        roles: response.data?.roles
      }));
      
      // 设置API默认请求头
      api.defaults.headers.common['Authorization'] = `Bearer ${normalizedToken}`;
      
      navigation.navigate('Main');
    } catch (error: any) {
      console.error('登录错误:', error);
      setLoginError('登录失败，请检查用户名和密码是否正确');
      if (error.response?.data?.message) {
        setLoginError(error.response.data.message);
      } else {
        setLoginError('用户名或密码错误');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <ImageBackground 
      source={require('../../assets/login-bg.png')} 
      style={styles.background}
    >
      <View style={styles.container}>
        <View style={styles.header}>
          <Text style={styles.title}>企业应用平台</Text>
          <Text style={styles.subtitle}>欢迎登录</Text>
        </View>
        
        {loginError && (
          <Text style={styles.errorText}>用户名或密码错误</Text>
        )}
        
        <View style={styles.inputContainer}>
          <UserOutlined style={styles.icon} name={DEFAULT_ICON_NAME} />
          <TextInput
            style={styles.input}
            placeholder="用户名"
            value={username}
            onChangeText={setUsername}
          />
        </View>
        
        <View style={styles.inputContainer}>
          <LockOutlined style={styles.icon} name={LOCK_ICON_NAME} />
          <TextInput
            style={styles.input}
            placeholder="密码"
            secureTextEntry
            value={password}
            onChangeText={setPassword}
          />
        </View>
        
        <TouchableOpacity 
          style={styles.button} 
          onPress={handleLogin}
          disabled={loading}
        >
          <Text style={styles.buttonText}>登 录</Text>
        </TouchableOpacity>
      </View>
    </ImageBackground>
  );
};

const styles = StyleSheet.create({
  background: {
    flex: 1,
    resizeMode: 'cover',
    justifyContent: 'center',
  },
  container: {
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
    marginHorizontal: 20,
    padding: 20,
    borderRadius: 10,
  },
  header: {
    alignItems: 'center',
    marginBottom: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  subtitle: {
    fontSize: 16,
    color: '#666',
  },
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 5,
    marginBottom: 15,
    paddingHorizontal: 10,
  },
  icon: {
    marginRight: 10,
  },
  input: {
    flex: 1,
    height: 40,
  },
  button: {
    backgroundColor: '#1890ff',
    height: 40,
    borderRadius: 5,
    justifyContent: 'center',
    alignItems: 'center',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
  },
  errorText: {
    color: 'red',
    textAlign: 'center',
    marginBottom: 15,
  },
});

export default Login;