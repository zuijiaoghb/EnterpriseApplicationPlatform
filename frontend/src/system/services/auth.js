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