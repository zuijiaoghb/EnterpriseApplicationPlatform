package com.enterprise.platform.user.service.impl;

import com.enterprise.platform.user.model.User;
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
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
        
        // 保存用户基本信息
        User savedUser = userRepository.save(user);
        
        // 如果有关联角色，保存角色关系
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            savedUser.setRoles(user.getRoles());
            return userRepository.save(savedUser);
        }
        
        return savedUser;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
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
}