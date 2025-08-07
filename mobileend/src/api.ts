import axios from 'axios';
import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
// 已移除useNavigation导入，使用NavigationService.navigate代替
import { navigate } from './navigation/NavigationService';

// 用于防止重复刷新令牌的标志
let isRefreshing = false;
// 存储等待刷新令牌的请求队列
let refreshSubscribers: ((token: string) => void)[] = [];

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
    } else if (config.url.includes('/auth/login') || config.url.includes('/auth/refresh-token')) {
      // 如果是登录请求或刷新令牌请求，不添加token
      delete config.headers['Authorization']; 
    }
    else {
      // 如果没有token且不是登录或刷新令牌请求，重定向到登录
      await AsyncStorage.removeItem('token');
      await AsyncStorage.removeItem('refreshToken');
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
    // 如果是登录或刷新令牌响应，存储令牌
    if ((response.config.url?.includes('/auth/login') || response.config.url?.includes('/auth/refresh-token')) && response.data?.access_token) {
      AsyncStorage.setItem('token', response.data.access_token);
      if (response.data.refresh_token) {
        AsyncStorage.setItem('refreshToken', response.data.refresh_token);
      }
    }
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    // 处理401错误（令牌过期）
    if (error.response?.status === 401 && !originalRequest._retry) {
      // 如果已经在刷新令牌，将请求加入等待队列
      if (isRefreshing) {
        return new Promise((resolve) => {
          refreshSubscribers.push((token: string) => {
            originalRequest.headers['Authorization'] = `Bearer ${token}`;
            resolve(api(originalRequest));
          });
        });
      }

      // 标记为正在刷新
      originalRequest._retry = true;
      isRefreshing = true;

      try {
        // 获取刷新令牌
        const refreshToken = await AsyncStorage.getItem('refreshToken');

        if (!refreshToken) {
          // 没有刷新令牌，跳转到登录
          await AsyncStorage.removeItem('token');
          await AsyncStorage.removeItem('refreshToken');
          navigate('Login', { param1: '', param2: 0 });
          return Promise.reject(error);
        }

        // 调用刷新令牌接口
        const response = await api.post('/auth/refresh-token', {
          refresh_token: refreshToken
        });

        if (response.data?.access_token) {
          // 存储新的访问令牌和刷新令牌
          await AsyncStorage.setItem('token', response.data.access_token);
          if (response.data.refresh_token) {
            await AsyncStorage.setItem('refreshToken', response.data.refresh_token);
          }

          // 通知所有等待的请求
          refreshSubscribers.forEach((callback) => callback(response.data.access_token));
          refreshSubscribers = [];

          // 重试原始请求
          originalRequest.headers['Authorization'] = `Bearer ${response.data.access_token}`;
          return api(originalRequest);
        } else {
          // 刷新令牌失败
          await AsyncStorage.removeItem('token');
          await AsyncStorage.removeItem('refreshToken');
          navigate('Login', { param1: '', param2: 0 });
          return Promise.reject(error);
        }
      } catch (refreshError) {
        // 刷新令牌过程中发生错误
        await AsyncStorage.removeItem('token');
        await AsyncStorage.removeItem('refreshToken');
        navigate('Login', { param1: '', param2: 0 });
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    } else if (error.response?.status === 403) {
      // 403错误（权限不足），跳转到登录
      await AsyncStorage.removeItem('token');
      await AsyncStorage.removeItem('refreshToken');
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
  const refreshToken = await AsyncStorage.getItem('refreshToken');

  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expirationTime = payload.exp * 1000;
      const currentTime = Date.now();
      const timeLeft = expirationTime - currentTime;

      // 当令牌即将过期（1分钟内）且有刷新令牌时，自动刷新
      if (timeLeft <= 60000 && refreshToken && !isRefreshing) {
        try {
          // 调用刷新令牌接口
          const response = await api.post('/auth/refresh-token', {
            refresh_token: refreshToken
          });

          if (response.data?.access_token) {
            // 存储新的访问令牌和刷新令牌
            await AsyncStorage.setItem('token', response.data.access_token);
            if (response.data.refresh_token) {
              await AsyncStorage.setItem('refreshToken', response.data.refresh_token);
            }
          }
        } catch (error) {
          console.error('自动刷新令牌失败:', error);
          await AsyncStorage.removeItem('token');
          await AsyncStorage.removeItem('refreshToken');
          navigate('Login', { param1: '', param2: 0 });
        }
      } else if (timeLeft <= 0) {
        // 令牌已过期
        await AsyncStorage.removeItem('token');
        await AsyncStorage.removeItem('refreshToken');
        navigate('Login', { param1: '', param2: 0 });
      }
    } catch (error) {
      console.error('解析token失败:', error);
      await AsyncStorage.removeItem('token');
      await AsyncStorage.removeItem('refreshToken');
      navigate('Login', { param1: '', param2: 0 });
    }
  } else if (refreshToken) {
    // 有刷新令牌但没有访问令牌，尝试刷新
    if (!isRefreshing) {
      try {
        const response = await api.post('/auth/refresh-token', {
          refresh_token: refreshToken
        });

        if (response.data?.access_token) {
          await AsyncStorage.setItem('token', response.data.access_token);
          if (response.data.refresh_token) {
            await AsyncStorage.setItem('refreshToken', response.data.refresh_token);
          }
        } else {
          await AsyncStorage.removeItem('refreshToken');
          navigate('Login', { param1: '', param2: 0 });
        }
      } catch (error) {
        console.error('使用刷新令牌获取访问令牌失败:', error);
        await AsyncStorage.removeItem('refreshToken');
        navigate('Login', { param1: '', param2: 0 });
      }
    }
  }
}

// 在应用初始化时检查
checkTokenExpiration();

// 设置定时器定期检查（每分钟）
setInterval(checkTokenExpiration, 60000);

export default api;