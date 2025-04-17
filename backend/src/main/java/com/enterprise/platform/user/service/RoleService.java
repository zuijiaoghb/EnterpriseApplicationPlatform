package com.enterprise.platform.user.service;

import com.enterprise.platform.user.model.Permission;
import com.enterprise.platform.user.model.Role;
import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    
    Role updateRolePermissions(Long roleId, Set<Long> permissionIds);
    Set<Permission> getRolePermissions(Long roleId);
}