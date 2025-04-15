package com.enterprise.platform.user.controller;

import com.enterprise.platform.user.dto.UserCreateRequest;
import com.enterprise.platform.user.model.Role;
import com.enterprise.platform.user.model.User;
import com.enterprise.platform.user.service.UserService;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserCreateRequest request) {
        // 使用专门的DTO接收请求数据
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        // 其他字段设置...
        
        // 处理角色
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = request.getRoleIds().stream()
                .map(roleId -> {
                    Role role = new Role();
                    role.setId(roleId);
                    return role;
                })
                .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody @Valid UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        
        // 处理角色
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = request.getRoleIds().stream()
                .map(roleId -> {
                    Role role = new Role();
                    role.setId(roleId);
                    return role;
                })
                .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        
        // 设置默认状态和创建时间
        user.setStatus(1); // 默认状态为1
        user.setCreateTime(LocalDateTime.now()); // 当前系统时间
        
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<User> updateUserRoles(
            @PathVariable Long id, 
            @RequestBody Set<Long> roleIds) {
        return ResponseEntity.ok(userService.updateUserRoles(id, roleIds));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(
        @RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsernameExists(
        @RequestParam String username,
        @RequestParam(required = false) Long excludeUserId) {
        boolean exists = userService.existsByUsername(username, excludeUserId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}