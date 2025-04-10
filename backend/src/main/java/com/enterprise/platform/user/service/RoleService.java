package com.enterprise.platform.user.service;

import com.enterprise.platform.user.model.Role;
import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
}