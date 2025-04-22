package com.enterprise.platform.user.service.impl;

import com.enterprise.platform.user.model.Role;
import com.enterprise.platform.user.model.User;
import com.enterprise.platform.user.repository.RoleRepository;
import com.enterprise.platform.user.repository.UserRepository;
import com.enterprise.platform.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<User> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        // 检查密码是否为空
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        // 加密密码
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(1); // 1表示启用状态
        }
        
        // 设置创建时间为当前系统时间
        // 修改为使用系统默认时区的当前时间
        // 修改为使用ZonedDateTime获取北京时间
        user.setCreateTime(java.time.ZonedDateTime.now(java.time.ZoneId.of("Asia/Shanghai")).toLocalDateTime());
        
        // 保存用户基本信息
        User savedUser = userRepository.save(user);
        
        // 如果有关联角色，保存角色关系
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            savedUser.setRoles(user.getRoles());
            return userRepository.save(savedUser);
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByCode("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("默认角色未配置"));
            user.setRoles(Set.of(defaultRole));
        }
        
        return savedUser;
    }

    @Override
    @Transactional  
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // 更新基础字段
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setStatus(user.getStatus());
        existingUser.setCreateTime(user.getCreateTime());
        
        // 更新密码（如果有）
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // 更新角色
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existingUser.setRoles(user.getRoles());
        }
        
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User updateUserRoles(Long id, Set<Long> roleIds) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 这里需要实现角色更新逻辑
        return userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username,Long excludeUserId) {
        return userRepository.existsByUsername(username, excludeUserId);
    }
}