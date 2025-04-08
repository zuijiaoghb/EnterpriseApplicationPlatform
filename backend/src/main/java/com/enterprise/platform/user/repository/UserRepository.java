package com.enterprise.platform.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.platform.user.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 根据用户名查找用户
    Optional<User> findByUsername(String username);
    
    // 检查用户名是否存在
    boolean existsByUsername(String username);
    
    // 根据邮箱查找用户（可选）
    Optional<User> findByEmail(String email);
}