const roles = {
  OPERATOR: ['equipment:view', 'maintenance:report'],
  MAINTENANCE: ['equipment:edit', 'maintenance:*'],
  ADMIN: ['*']
};

const PermissionWrapper = ({ children, requiredPermission }) => {
  const { user } = useAuth();
  
  if (!hasPermission(user.role, requiredPermission)) {
    return <Result status="403" title="操作权限不足" />;
  }
  
  return children;
};