package com.enterprise.platform.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.enterprise.platform.system.model.OAuth2Client;
import com.enterprise.platform.system.service.CaptchaService;
import com.enterprise.platform.system.service.OAuth2ClientService;
import com.enterprise.platform.user.dto.LoginRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import com.enterprise.platform.user.model.RefreshToken;
import com.enterprise.platform.user.model.User;
import com.enterprise.platform.user.repository.UserRepository;
import com.enterprise.platform.user.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // 确保导入了正确的UserDetails类
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtEncodingException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@CrossOrigin(origins = {"http://192.168.21.175:3001", "http://localhost:8081"}, allowCredentials = "true")
@RestController
@RequestMapping("/auth")
@Transactional(transactionManager = "mysqlTransactionManager")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${jwt.expiration}")
    private long jwtExpiration; // 令牌过期时间(秒)

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2ClientService oAuth2ClientService;

    private final RefreshTokenService refreshTokenService;    
    private final UserRepository userRepository;
    private final CaptchaService captchaService;
        

    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,
                          RefreshTokenService refreshTokenService, 
                          UserRepository userRepository, PasswordEncoder passwordEncoder,
                          OAuth2ClientService oAuth2ClientService, ClientRegistrationRepository clientRegistrationRepository,
                          CaptchaService captchaService) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenService = refreshTokenService;        
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.oAuth2ClientService = oAuth2ClientService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.captchaService = captchaService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 验证码验证
            if (loginRequest.getCaptchaId() == null || loginRequest.getCaptcha() == null || 
                loginRequest.getCaptcha().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", 400, "message", "验证码不能为空"));
            }
            
            boolean isValidCaptcha = captchaService.validateCaptcha(
                loginRequest.getCaptchaId(), 
                loginRequest.getCaptcha()
            );
            
            if (!isValidCaptcha) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", 400, "message", "验证码错误或已过期"));
            }
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            logger.info("Generating JWT Token for user: {}", authentication.getName());
            
            // 构建JWT时明确指定密钥
            // 添加JWS Header配置
            JwsHeader jwsHeaders = JwsHeader.with(() -> "HS256")
                    .type("JWT")
                    .keyId("HS256-KEY")
                    .build();               
            
            JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                    .issuer("enterprise-platform")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(Duration.ofSeconds(jwtExpiration)))
                    .subject(authentication.getName())
                    .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(auth -> auth.startsWith("ROLE_"))
                        //.map(auth -> auth.substring(5))
                        .collect(Collectors.toList()))
                    .build();
            
            // 使用带Header的JwtEncoderParameters
            Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeaders, claimsSet));
            
            logger.debug("JWT generated successfully");
            
            // 生成刷新令牌
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 使用注入的userRepository查找用户
            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userDetails.getUsername()));
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt.getTokenValue())
                .body((Map<String, Object>) Map.of(
                    "status", 200,
                    "message", "登录成功",
                    "access_token", jwt.getTokenValue(),
                    "token_type", "Bearer",
                    "expires_in", jwtExpiration,
                    "refresh_token", refreshToken.getToken(),
                    "user", (Map<String, Object>) Map.of(
                        "username", authentication.getName(),
                        "roles", authentication.getAuthorities()
                    )                
                ));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("status", 401, "message", "认证失败"));
        } catch (JwtEncodingException e) {
            logger.error("JWT encoding failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", 500, "message", "令牌生成失败"));
        } catch (Exception e) {
            logger.error("Unexpected error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", 500, "message", "系统错误"));
        }
    }
    
    @PreAuthorize("isAuthenticated()")  // 要求已认证用户才能访问
    @GetMapping("/info") 
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ResponseEntity.ok(principal);
        } else if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return ResponseEntity.ok(Map.of(
                "username", jwt.getSubject(),
                "roles", jwt.getClaimAsStringList("roles") // 从JWT claims中获取roles
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "username", principal.toString(),
                "roles", authentication.getAuthorities()
            ));
        }
    }

    @PreAuthorize("isAuthenticated()")  // 要求已认证用户才能访问
    @GetMapping("/check-role")
    public ResponseEntity<?> checkUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = ((Jwt) (authentication.getPrincipal())).getClaimAsStringList("roles").contains("ROLE_ADMIN");
        
        return ResponseEntity.ok(Map.of("hasAdminRole", hasAdminRole));
    }

    @PostMapping("/refresh-token")    
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshTokenStr = request.get("refresh_token");
        
        if (refreshTokenStr == null || refreshTokenStr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( Map.of("status", 400, "message", "刷新令牌不能为空"));
        }
        
        try {
            return refreshTokenService.findByToken(refreshTokenStr)
                .map(token -> {
                    if (!refreshTokenService.validateRefreshToken(token)) {
                        refreshTokenService.revokeRefreshToken(token);
                        Map<String, Object> response = new HashMap<>();
                        response.put("status", 401);
                        response.put("message", "刷新令牌无效或已过期");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                    }
                    
                    User user = token.getUser();
                    
                    // 构建新的访问令牌
                    JwsHeader jwsHeaders = JwsHeader.with(() -> "HS256")
                            .type("JWT")
                            .keyId("HS256-KEY")
                            .build();                
                    
                    JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                            .issuer("enterprise-platform")
                            .issuedAt(Instant.now())
                            .expiresAt(Instant.now().plus(Duration.ofSeconds(jwtExpiration)))
                            .subject(user.getUsername())
                            .claim("roles", user.getRoles().stream()
                                .map(role -> role.getCode())
                                .collect(Collectors.toList()))
                            .build();
                    
                    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeaders, claimsSet));
                    
                    // 生成新的刷新令牌
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
                    
                    Map<String, Object> responseBody = new HashMap<>();
                    responseBody.put("status", 200);
                    responseBody.put("message", "令牌刷新成功");
                    responseBody.put("access_token", jwt.getTokenValue());
                    responseBody.put("token_type", "Bearer");
                    responseBody.put("expires_in", jwtExpiration);
                    responseBody.put("refresh_token", newRefreshToken.getToken());
                    
                    return ResponseEntity.ok().body(responseBody);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "无效的刷新令牌")));
        } catch (Exception e) {
            logger.error("刷新令牌过程中发生错误", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", 500, "message", "系统错误，请稍后重试"));
        }
    }

    @PostMapping("/oauth2/token")
    public ResponseEntity<Map<String, Object>> getTokenByClientCredentials(
        @RequestParam("grant_type") String grantType,
        @RequestParam("client_id") String clientId,
        @RequestParam("client_secret") String clientSecret) {
        
        try {            
            ClientRegistration client = clientRegistrationRepository.findByRegistrationId(clientId);
            if (client == null || !passwordEncoder.matches(clientSecret, client.getClientSecret())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid_client"));
            }
            
            // 新增：从数据库查询客户端状态
            OAuth2Client oauth2Client = oAuth2ClientService.getClientById(clientId);
            if (oauth2Client == null || !oauth2Client.getActive()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid_client"));
            }
    
            // 设置令牌过期时间
            Instant now = Instant.now();
            Instant expiresAt = now.plus(Duration.ofSeconds(jwtExpiration));

            // 构建JWT时明确指定密钥
            // 添加JWS Header配置
            JwsHeader jwsHeaders = JwsHeader.with(() -> "HS256")
                    .type("JWT")
                    .keyId("HS256-KEY")
                    .build();  
            
            JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("enterprise-platform")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(clientId)
                .claim("roles", Collections.singletonList("ROLE_CLIENT"))
                .claim("scopes", client.getScopes()) // 添加scope声明
                .build();
                    
            // 使用带Header的JwtEncoderParameters
            Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeaders, claimsSet));
            
            return ResponseEntity.ok()
                .body(Map.of(
                    "access_token", jwt.getTokenValue(),
                    "token_type", "Bearer",
                    "expires_in", Duration.between(now, expiresAt).getSeconds(),
                    "scope", String.join(" ", client.getScopes())
                ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "invalid_client"));
        } catch (Exception e) {
            logger.error("Client credentials token generation failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "server_error"));
        }
    }
}