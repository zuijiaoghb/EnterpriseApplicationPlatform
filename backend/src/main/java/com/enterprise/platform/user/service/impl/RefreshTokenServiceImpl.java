package com.enterprise.platform.user.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.platform.user.model.RefreshToken;
import com.enterprise.platform.user.model.User;
import com.enterprise.platform.user.repository.RefreshTokenRepository;
import com.enterprise.platform.user.repository.UserRepository;
import com.enterprise.platform.user.service.RefreshTokenService;
import com.enterprise.platform.util.AesEncryptUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final AesEncryptUtil aesEncryptUtil;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, AesEncryptUtil aesEncryptUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.aesEncryptUtil = aesEncryptUtil;
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public RefreshToken createRefreshToken(User user) {
        // 先删除用户已有的刷新令牌
        refreshTokenRepository.deleteByUserId(user.getId());

        // 创建新的刷新令牌
        RefreshToken refreshToken = new RefreshToken();
        String plainToken = UUID.randomUUID().toString();
        try {
            // 加密令牌
            String encryptedToken = aesEncryptUtil.encrypt(plainToken);
            refreshToken.setToken(encryptedToken);
        } catch (Exception e) {
            log.error("Error encrypting refresh token", e);
            throw new RuntimeException("Failed to create refresh token", e);
        }
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(Duration.ofSeconds(refreshExpiration)));
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        try {
            // 直接使用传入的令牌进行查询（已加密）
            return refreshTokenRepository.findByToken(token);
        } catch (Exception e) {
            log.error("Error looking up token", e);
            return Optional.empty();
        }
    }

    @Override
    public boolean validateRefreshToken(RefreshToken token) {
        // 检查令牌是否已过期或已被撤销
        return !token.isRevoked() && token.getExpiresAt().isAfter(Instant.now());
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public RefreshToken revokeRefreshToken(RefreshToken token) {
        token.setRevoked(true);
        return refreshTokenRepository.save(token);
    }

    @Override
    @Transactional(transactionManager = "mysqlTransactionManager")
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}