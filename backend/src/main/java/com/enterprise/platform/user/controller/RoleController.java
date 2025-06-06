package com.enterprise.platform.user.controller;

import com.enterprise.platform.user.model.Permission;
import com.enterprise.platform.user.model.Role;
import com.enterprise.platform.user.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(id, role));
    }

    @PutMapping("/{id}/permissions")
    public ResponseEntity<Role> updateRolePermissions(
        @PathVariable Long id,
        @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.updateRolePermissions(id, permissionIds));
    }

    @GetMapping("/{id}/permissions")
    public ResponseEntity<Set<Permission>> getRolePermissions(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRolePermissions(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}