package com.enterprise.platform.user.controller;

import com.enterprise.platform.user.dto.PermissionTreeDTO;
import com.enterprise.platform.user.model.Permission;
import com.enterprise.platform.user.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Transactional(transactionManager = "mysqlTransactionManager")
public class PermissionController {
    
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        if (permission.getParentId() != null) {
            Permission parent = permissionService.findById(permission.getParentId())
                .orElseThrow(() -> new RuntimeException("父权限不存在"));
            permission.setParent(parent);
        }
        return ResponseEntity.ok(permissionService.create(permission));
    }

    @GetMapping("/tree")
    public ResponseEntity<List<PermissionTreeDTO>> getPermissionTree() {
        List<Permission> permissions = permissionService.findAllRootPermissions();
        List<PermissionTreeDTO> tree = permissions.stream()
            .map(this::convertToTreeDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(tree);
    }

    
    private PermissionTreeDTO convertToTreeDTO(Permission permission) {
        PermissionTreeDTO dto = new PermissionTreeDTO();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setCode(permission.getCode());
        dto.setDescription(permission.getDescription());
        
        // 递归处理子权限
        if (!permission.getChildren().isEmpty()) {
            dto.setChildren(permission.getChildren().stream()
                .map(this::convertToTreeDTO)
                .collect(Collectors.toList()));
        }
        return dto;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(
            @PathVariable Long id, 
            @RequestBody Permission permission) {
        // 获取现有权限
        Permission existingPermission = permissionService.findById(id)
            .orElseThrow(() -> new RuntimeException("权限不存在"));
        
        // 更新基础字段
        existingPermission.setName(permission.getName());
        existingPermission.setCode(permission.getCode());
        existingPermission.setDescription(permission.getDescription());
        
        // 处理父权限
        if (permission.getParentId() != null) {
            Permission parent = permissionService.findById(permission.getParentId())
                .orElseThrow(() -> new RuntimeException("父权限不存在"));
            existingPermission.setParent(parent);
        } else {
            existingPermission.setParent(null);
        }
        
        return ResponseEntity.ok(permissionService.update(id, existingPermission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
