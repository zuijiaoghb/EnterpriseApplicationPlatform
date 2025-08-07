import axios from 'axios';
import { message } from 'antd';

// 用于防止重复刷新令牌的标志
let isRefreshing = false;
// 存储等待刷新令牌的请求队列
let refreshSubscribers = [];

const isExternal = window.location.hostname!== '192.168.21.175';
const api = axios.create({
  baseURL: isExternal? 'http://oa.jiangte.com.cn:8081' : 'http://192.168.21.175:8081',
  timeout: 10000,
  withCredentials: true, // 必须与后端setAllowCredentials(true)对应
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    'X-Requested-With': 'XMLHttpRequest' // 添加识别头
  }
});


// 添加请求拦截器
// 在请求拦截器中添加token检查
api.interceptors.request.use(config => {
  config.headers = config.headers || {};
  const token = localStorage.getItem('token');
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
    config.headers['Content-Type'] = 'application/json';
  } else if (config.url.includes('/auth/login') || config.url.includes('/auth/refresh-token')) {
    // 如果是登录请求或刷新令牌请求，不添加token
    delete config.headers['Authorization'];
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// 添加响应拦截器
api.interceptors.response.use(response => {
  // 确保能获取到Authorization头
  if (response.headers['authorization']) {
    response.headers['Authorization'] = response.headers['authorization'];
  }
  // 如果是登录或刷新令牌响应，存储令牌
  if ((response.config.url?.includes('/auth/login') || response.config.url?.includes('/auth/refresh-token')) && response.data?.access_token) {
    localStorage.setItem('token', response.data.access_token);
    if (response.data.refresh_token) {
      localStorage.setItem('refreshToken', response.data.refresh_token);
    }
  }
  return response;
}, error => {
  const originalRequest = error.config;

  // 处理401错误（令牌过期）
  if (error.response?.status === 401 && !originalRequest._retry) {
    // 如果已经在刷新令牌，将请求加入等待队列
    if (isRefreshing) {
      return new Promise((resolve) => {
        refreshSubscribers.push((token) => {
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
      const refreshToken = localStorage.getItem('refreshToken');

      if (!refreshToken) {
        // 没有刷新令牌，跳转到登录
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(error);
      }

      // 调用刷新令牌接口
      return api.post('/auth/refresh-token', {
        refresh_token: refreshToken
      }).then((response) => {
        if (response.data?.access_token) {
          // 存储新的访问令牌和刷新令牌
          localStorage.setItem('token', response.data.access_token);
          if (response.data.refresh_token) {
            localStorage.setItem('refreshToken', response.data.refresh_token);
          }

          // 通知所有等待的请求
          refreshSubscribers.forEach((callback) => callback(response.data.access_token));
          refreshSubscribers = [];

          // 重试原始请求
          originalRequest.headers['Authorization'] = `Bearer ${response.data.access_token}`;
          return api(originalRequest);
        } else {
          // 刷新令牌失败
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          window.location.href = '/login';
          return Promise.reject(error);
        }
      }).catch((refreshError) => {
        // 刷新令牌过程中发生错误
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }).finally(() => {
        isRefreshing = false;
      });
    } catch (e) {
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      window.location.href = '/login';
      return Promise.reject(e);
    }
  } else if (error.response?.status === 403) {
    // 权限不足，显示错误消息但不重定向
    message.error('权限不足，无法访问该资源');
  }
  return Promise.reject(error);
});

// 检查令牌是否过期的函数
function checkTokenExpiration() {  
  const token = localStorage.getItem('token');
  const refreshToken = localStorage.getItem('refreshToken');

  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expirationTime = payload.exp * 1000; // 转换为毫秒
      const currentTime = Date.now();
      const timeLeft = expirationTime - currentTime;

      // 当令牌即将过期（1分钟内）且有刷新令牌时，自动刷新
      if (timeLeft <= 60000 && refreshToken && !isRefreshing) {
        try {
          // 调用刷新令牌接口
          api.post('/auth/refresh-token', {
            refresh_token: refreshToken
          }).then((response) => {
            if (response.data?.access_token) {
              // 存储新的访问令牌和刷新令牌
              localStorage.setItem('token', response.data.access_token);
              if (response.data.refresh_token) {
                localStorage.setItem('refreshToken', response.data.refresh_token);
              }
              message.success('登录已自动续期');
            }
          }).catch((error) => {
            console.error('自动刷新令牌失败:', error);
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            window.location.href = '/login';
          });
        } catch (error) {
          console.error('自动刷新令牌失败:', error);
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          window.location.href = '/login';
        }
      } else if (timeLeft > 0 && timeLeft < 5 * 60 * 1000) {
        // 当令牌将在5分钟内过期时，提示用户
        message.warning('登录即将过期，将自动续期或请刷新页面');
      } else if (timeLeft <= 0) {
        // 令牌已过期
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
      }
    } catch (error) {
      console.error('解析token失败:', error);
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      window.location.href = '/login';
    }
  } else if (refreshToken) {
    // 有刷新令牌但没有访问令牌，尝试刷新
    if (!isRefreshing) {
      try {
        api.post('/auth/refresh-token', {
          refresh_token: refreshToken
        }).then((response) => {
          if (response.data?.access_token) {
            localStorage.setItem('token', response.data.access_token);
            if (response.data.refresh_token) {
              localStorage.setItem('refreshToken', response.data.refresh_token);
            }
          } else {
            localStorage.removeItem('refreshToken');
            window.location.href = '/login';
          }
        }).catch((error) => {
          console.error('使用刷新令牌获取访问令牌失败:', error);
          localStorage.removeItem('refreshToken');
          window.location.href = '/login';
        });
      } catch (error) {
        console.error('使用刷新令牌获取访问令牌失败:', error);
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
      }
    }
  }
}

// 在应用初始化时检查
checkTokenExpiration();

// 设置定时器定期检查（每分钟）
setInterval(checkTokenExpiration, 60000);

export default api;