package com.enterprise.platform.user.service;

import com.enterprise.platform.user.model.Permission;
import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<Permission> findAll();
    Permission create(Permission permission);
    Permission update(Long id, Permission permission);
    void delete(Long id);
    List<Permission> getPermissionTree(); // 添加树形结构查询方法
    Optional<Permission> findById(Long id); // 添加根据ID查询方法
    List<Permission> findAllRootPermissions();
}