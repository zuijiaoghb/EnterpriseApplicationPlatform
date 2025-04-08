import axios from 'axios';

const api = axios.create({
  baseURL: 'http://192.168.21.175:8081',
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
  } else {
    // 如果没有token且不是登录请求，重定向到登录
    if (!config.url.includes('/auth/login')) {
      window.location.href = '/login';
    }
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
  return response;
}, error => {
  if (error.response?.status === 401 || error.response?.status === 403) {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }
  return Promise.reject(error.response?.data || error);
});

export default api;