package com.enterprise.platform.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enterprise.platform.user.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 根据用户名查找用户
    Optional<User> findByUsername(String username);
    
    // 检查用户名是否存在
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username AND (:excludeUserId IS NULL OR u.id != :excludeUserId)")
    boolean existsByUsername(@Param("username") String username, @Param("excludeUserId") Long excludeUserId);
    
    // 根据邮箱查找用户（可选）
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}