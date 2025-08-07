package com.enterprise.platform.user.service;

import com.enterprise.platform.user.model.RefreshToken;
import com.enterprise.platform.user.model.User;

import java.util.Optional;

public interface RefreshTokenService {
    /**
     * 创建刷新令牌
     * @param user 用户
     * @return 刷新令牌
     */
    RefreshToken createRefreshToken(User user);
    
    /**
     * 根据令牌字符串查找刷新令牌
     * @param token 令牌字符串
     * @return 刷新令牌 Optional
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * 验证刷新令牌是否有效
     * @param token 刷新令牌
     * @return 是否有效
     */
    boolean validateRefreshToken(RefreshToken token);
    
    /**
     * 撤销刷新令牌
     * @param token 刷新令牌
     * @return 撤销后的刷新令牌
     */
    RefreshToken revokeRefreshToken(RefreshToken token);
    
    /**
     * 根据用户ID删除刷新令牌
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);
}