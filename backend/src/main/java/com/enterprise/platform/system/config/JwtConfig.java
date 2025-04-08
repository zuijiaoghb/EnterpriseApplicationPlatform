package com.enterprise.platform.system.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    @PostConstruct
    public void validateConfig() {
        if(secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT secret必须至少32字符");
        }
        System.out.println("JWT配置验证通过");
    }
}
