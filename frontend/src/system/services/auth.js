import api from '../../api';

export const login = async (username, password) => {
  try {
    const response = await api.post('/auth/login', { username, password });
    localStorage.setItem('token', response.token);
    return response;
  } catch (error) {
    throw error;
  }
};

export const checkPermission = (requiredPermission) => {
  const user = JSON.parse(localStorage.getItem('user'));
  
  // 如果是admin用户，直接返回true
  if (user?.roles?.some(role => role.authority === 'ROLE_ADMIN')) {
    return true;
  }
  
  return user?.roles?.some(role => 
    role.permissions?.some(p => p.code === requiredPermission)
  );
};
