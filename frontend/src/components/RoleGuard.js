import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import api from '../api';
import { message } from 'antd';

class RoleGuard extends React.Component {
  state = {
    isAuthenticated: false,
    userRoles: [],
    loading: true
  };

  componentDidMount() {
    this.checkUserRole();
  }

  checkUserRole = async () => {
    try {
      const response = await api.get('/auth/info');
      const { roles } = response.data;
      this.setState({
        isAuthenticated: true,
        userRoles: roles || [],
        loading: false
      });
    } catch (error) {
      message.error('获取用户信息失败，请重新登录');
      this.setState({
        isAuthenticated: false,
        userRoles: [],
        loading: false
      });
    }
  };

  hasRequiredRole = () => {
    const { allowedRoles } = this.props;
    const { userRoles } = this.state;

    return allowedRoles.some(role => userRoles.includes(role) || userRoles.includes(`ROLE_${role}`));
  };

  render() {
    const { loading, isAuthenticated } = this.state;

    if (loading) {
      return <div>加载中...</div>;
    }

    if (!isAuthenticated) {
      return <Navigate to="/login" replace />;
    }

    if (!this.hasRequiredRole()) {
      return <Navigate to="/dashboard" replace />;
    }

    return <Outlet />;
  }
}

export default RoleGuard;