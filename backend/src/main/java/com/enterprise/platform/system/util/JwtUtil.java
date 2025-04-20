package com.enterprise.platform.system.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    public String generateToken(Authentication authentication) {
        System.out.println("开始生成JWT Token");
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Date now = new Date();
            
            // 添加secret有效性检查
            if(secret == null || secret.trim().isEmpty()) {
                throw new IllegalStateException("JWT secret未配置");
            }
            
            Date expiryDate = new Date(now.getTime() + expiration * 1000);
            
            String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(
                    io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()),
                    io.jsonwebtoken.SignatureAlgorithm.HS512 // 明确指定算法
                )
                .compact();
                
            System.out.println("成功生成JWT: " + token.substring(0, 10) + "..."); // 打印部分token
            String username = getUsernameFromToken(token);
            System.out.println("Token验证结果:" + username); 
            return token;
        } catch (Exception e) {
            System.err.println("生成JWT失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public String getUsernameFromToken(String token) {
        // 使用新的 Jwts.parserBuilder() 方法来替代已弃用的 Jwts.parser()
        return Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public boolean validateToken(String token, UserDetails userDetails) {
        // 在生成后立即验证
        String username = getUsernameFromToken(token);
        System.out.println("Token验证结果:" + username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    private boolean isTokenExpired(String token) {
        // 原方法未定义，添加获取过期时间的逻辑
        final Date expiration = Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
    
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthoritiesFromToken(String token) {
        io.jsonwebtoken.Claims claims = Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        @SuppressWarnings("unchecked")
        List<String> authorities = claims.get("authorities", List.class);
        
        return authorities.stream()
                .map(auth -> new org.springframework.security.core.authority.SimpleGrantedAuthority(auth))
                .collect(Collectors.toList());
    }
    
    public boolean validateToken(String token) {
        try {
            io.jsonwebtoken.Jws<io.jsonwebtoken.Claims> claims = Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
