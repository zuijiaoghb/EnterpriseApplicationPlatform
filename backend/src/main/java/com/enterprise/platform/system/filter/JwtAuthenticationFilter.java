package com.enterprise.platform.system.filter;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication; // 添加缺失的导入语句

import com.enterprise.platform.system.util.JwtUtil;
import com.enterprise.platform.user.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    
private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/auth/login");
        // 关键修复：显式设置authenticationManager
        super.setAuthenticationManager(authenticationManager);
    }
    
    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException {
        logger.info("JwtFilter开始认证，路径: {}", request.getRequestURI());
        try {
            LoginRequest creds = new ObjectMapper()
                .readValue(request.getInputStream(), LoginRequest.class);
            
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    creds.getUsername(),
                    creds.getPassword(),
                    new ArrayList<>()
                )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult
    ) throws IOException {
        logger.info("认证成功，用户: {}", authResult.getName());
        String token = jwtUtil.generateToken(authResult);
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        
        // 添加与Controller一致的响应体
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), Map.of(
            "status", 200,
            "message", "登录成功"
        ));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, 
        HttpServletResponse response, AuthenticationException failed) {
        try {
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"status\":401,\"message\":\"认证失败\",\"error\":\"%s\"}", 
                failed.getMessage()
            ));
        } catch (IOException e) {
            logger.error("写入响应失败", e);
        }
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        // 增强日志记录
        logger.info("收到请求 - 方法: {}, 路径: {}, Origin: {}, Content-Type: {}", 
            request.getMethod(), 
            request.getRequestURI(),
            request.getHeader("Origin"),
            request.getHeader("Content-Type"));
        
        // 特殊处理登录请求，直接放行
        if (request.getRequestURI().equals("/auth/login")) {
            chain.doFilter(request, response);
            return;
        }
            
        // JWT验证逻辑
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            logger.debug("找到Bearer Token: {}", token.substring(0, Math.min(token.length(), 10)) + "...");
            
            if (jwtUtil.validateToken(token, null)) {
                String username = jwtUtil.getUsernameFromToken(token);
                logger.info("Token验证成功 - 用户: {}", username);
                
                Authentication auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtUtil.getAuthoritiesFromToken(token)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                logger.warn("Token验证失败");
            }
        } else {
            logger.debug("未找到有效的Authorization头");
        }
        
        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            logger.error("过滤器链执行异常", e);
            throw new RuntimeException(e);
        }
    }
}
