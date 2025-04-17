package com.enterprise.platform.user.service.impl;

import com.enterprise.platform.user.model.Permission;
import com.enterprise.platform.user.model.Role;
import com.enterprise.platform.user.repository.RoleRepository;
import com.enterprise.platform.user.repository.PermissionRepository;
import com.enterprise.platform.user.service.RoleService;
import com.enterprise.platform.user.service.AuditService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AuditService auditService;
    private final PermissionRepository permissionRepository; // 新增

    public RoleServiceImpl(RoleRepository roleRepository, 
                          AuditService auditService,
                          PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.auditService = auditService;
        this.permissionRepository = permissionRepository; // 新增
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id, Role role) {
        role.setId(id);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRolePermissions(Long roleId, Set<Long> permissionIds) {
        // 记录变更日志
        auditService.logPermissionChange(
            SecurityContextHolder.getContext().getAuthentication().getName(),
            roleId,
            permissionIds
        );
        
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        // 获取Permission对象集合
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
        role.setPermissions(permissions);
        
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Set<Permission> getRolePermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("角色不存在"));
        return role.getPermissions();
    }
}