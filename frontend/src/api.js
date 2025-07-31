import axios from 'axios';
import { message } from 'antd';

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
  const token = localStorage.getItem('token');
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
    config.headers['Content-Type'] = 'application/json';
  } 
  // 不立即重定向，让请求继续，由响应拦截器处理错误
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
  return response;
}, error => {
  if (error.response?.status === 401) {
    // 未授权，可能是token过期或无效
    localStorage.removeItem('token');
    if (!error.response.config.url.includes('/auth/login')) {
      window.location.href = '/login';
    }
  } else if (error.response?.status === 403) {
    // 权限不足，显示错误消息但不重定向
    message.error('权限不足，无法访问该资源');
  }
  return Promise.reject(error);
});

export default api;