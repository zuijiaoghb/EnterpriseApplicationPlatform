package com.enterprise.platform.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.platform.user.model.RefreshToken;
import com.enterprise.platform.user.model.User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    /**
     * 根据令牌字符串查找刷新令牌
     * @param token 令牌字符串
     * @return 刷新令牌 Optional
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * 根据用户查找刷新令牌
     * @param user 用户
     * @return 刷新令牌 Optional
     */
    Optional<RefreshToken> findByUser(User user);
    
    /**
     * 根据用户ID删除刷新令牌
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);
}