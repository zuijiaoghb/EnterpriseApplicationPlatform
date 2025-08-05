import axios from 'axios';
import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
// 已移除useNavigation导入，使用NavigationService.navigate代替
import { navigate } from './navigation/NavigationService';

// 根据运行环境设置基础URL
const baseURL = Platform.OS === 'web' 
  ? process.env.REACT_APP_API_URL 
  : process.env.API_URL || 'http://192.168.21.175:8081';

const api = axios.create({
  baseURL,
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'X-Requested-With': 'XMLHttpRequest'
  }
});

// 请求拦截器
api.interceptors.request.use(
  async (config: any) => {
    // 在这里可以添加请求前的处理逻辑
    config.headers = config.headers || {};
    const token = await AsyncStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
      config.headers['Content-Type'] = 'application/json';
    } else if (config.url.includes('/auth/login')) {
      // 如果是登录请求，不添加token
      delete config.headers['Authorization']; 
    }
    else if (!config.url.includes('/auth/login')) {
      // 如果没有token且不是登录请求，重定向到登录
      await AsyncStorage.removeItem('token');
      navigate('Login', { param1: '', param2: 0 });
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    // 确保能获取到Authorization头
    if (response.headers['authorization']) {
      response.headers['Authorization'] = response.headers['authorization'];
    }
    return response;
  },
  async (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      await AsyncStorage.removeItem('token');
      navigate('Login', { param1: '', param2: 0 });
   }
    // 修改错误处理，确保返回完整响应
    if (error.response) {
      if (!error.response.config.url.includes('/auth/login')) {
        // 移动端使用导航跳转而不是window.location
        // 需要导入navigation对象
      }
      return Promise.reject(error); // 返回完整错误对象
    }
    return Promise.reject(error);
  }
);

// 添加主动检查令牌过期的机制
async function checkTokenExpiration() {
  const token = await AsyncStorage.getItem('token');
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expirationTime = payload.exp * 1000;
      const currentTime = Date.now();
      const timeLeft = expirationTime - currentTime;

      if (timeLeft <= 0) {
        await AsyncStorage.removeItem('token');
        navigate('Login', { param1: '', param2: 0 });
      }
    } catch (error) {
      console.error('解析token失败:', error);
      await AsyncStorage.removeItem('token');
      navigate('Login', { param1: '', param2: 0 });
    }
  }
}

// 在应用初始化时检查
checkTokenExpiration();

// 设置定时器定期检查（每分钟）
setInterval(checkTokenExpiration, 60000);

export default api;