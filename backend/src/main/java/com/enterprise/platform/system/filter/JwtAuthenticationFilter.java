package com.enterprise.platform.system.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication; // 添加缺失的导入语句

import com.enterprise.platform.system.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthenticationFilter extends OncePerRequestFilter {  // 改为继承OncePerRequestFilter    
        
        private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
        
        private final JwtUtil jwtUtil;       

        // 添加构造函数注入
        public JwtAuthenticationFilter(JwtUtil jwtUtil) {            
            this.jwtUtil = jwtUtil;
        }    

        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain chain) throws IOException, ServletException {
            // 明确放行登录请求
            if ("/auth/login".equals(request.getRequestURI()) && 
                "POST".equalsIgnoreCase(request.getMethod())) {
                chain.doFilter(request, response);
                return;
            }            
            
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

                if (jwtUtil.validateToken(token)) {
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
