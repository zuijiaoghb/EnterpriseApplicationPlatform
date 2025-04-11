package com.enterprise.platform.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.enterprise.platform.system.util.JwtUtil;
import com.enterprise.platform.user.dto.LoginRequest;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // 确保导入了正确的UserDetails类
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;


@CrossOrigin(origins = "http://192.168.21.175:3000", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    
    @PreAuthorize("permitAll()")
    @PostMapping("/login") // 添加明确的路径映射
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("开始认证用户: {}", loginRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            logger.info("认证成功，开始生成JWT");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(authentication);
            
            logger.info("生成的JWT: {}", jwt);
            logger.info("设置的Authorization头: Bearer {}", jwt);
            
            return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt)
                .header("Access-Control-Expose-Headers", "Authorization")
                .body(Map.of(
                    "status", 200,
                    "message", "登录成功"
                ));
        } catch (org.springframework.security.core.AuthenticationException e) {
            logger.error("认证失败", e);
            return ResponseEntity.status(401).body(Map.of(
                "status", 401,
                "message", "认证失败"
            ));
        }
    }
    
    @PreAuthorize("isAuthenticated()")  // 要求已认证用户才能访问
    @GetMapping("/info") 
    public ResponseEntity<?> getUserInfo() {
        Object principal = SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        
        // 处理principal可能是String或UserDetails的情况
        if (principal instanceof UserDetails) {
            return ResponseEntity.ok(principal);
        } else {
            // 当principal是字符串时(如JWT subject)
            return ResponseEntity.ok(Map.of(
                "username", principal.toString(),
                "roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities()  // 获取用户实际角色列表
            ));
        }
    }

    @GetMapping("/check-role")
    public ResponseEntity<?> checkUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        
        return ResponseEntity.ok(Map.of("hasAdminRole", hasAdminRole));
    }

}