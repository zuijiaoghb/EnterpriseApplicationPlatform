import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function AuthGuard({ children }) {
  const navigate = useNavigate();
  
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    }
  }, [navigate]);

  // 在return前添加：
  if (!localStorage.getItem('token')) {
    return <div>验证中...</div>;
  }
  return <>{children}</>;
}