package com.enterprise.platform.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.web.cors.CorsConfigurationSource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.springframework.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.enterprise.platform.system.repository.JdbcClientRegistrationRepository;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.proc.SecurityContext;
import javax.crypto.spec.SecretKeySpec; // 导入缺失的类
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableAutoConfiguration(exclude = {
    OAuth2ResourceServerAutoConfiguration.class,  // 排除OAuth2资源服务器自动配置
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class  // 排除默认的安全配置    
})
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    JdbcClientRegistrationRepository repository;

    @Value("${jwt.secret}")
    private String jwtSecret;  // 将JWT密钥配置移动到类字段

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }    

    // 新增CORS配置Bean（必须）
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.info("创建CORS配置，允许来源: http://192.168.21.175:3000");

        CorsConfiguration config = new CorsConfiguration();
        // 使用具体的前端地址
        config.setAllowedOriginPatterns(Arrays.asList(
            "http://192.168.21.175:3000",
            "http://localhost:3000"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(Arrays.asList("Authorization"));
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    
    @Bean
    @Primary  // 添加@Primary注解确保优先使用这个Bean
    public JwtEncoder jwtEncoder() {  // 移除方法参数
        logger.info("Initializing JWT Encoder with secret key length: {}", jwtSecret.length());
        
        try {
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 32) {
                throw new IllegalArgumentException("JWT secret key must be at least 32 bytes");
            }
        
            SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
            JWK jwk = new OctetSequenceKey.Builder(key)
                    .keyID("HS256-KEY")
                    .algorithm(JWSAlgorithm.HS256)
                    .keyUse(KeyUse.SIGNATURE)
                    .build();
        
            JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
            return new NimbusJwtEncoder(jwkSource);
        } catch (Exception e) {
            logger.error("Failed to initialize JWT Encoder", e);
            throw new RuntimeException("JWT Encoder initialization failed", e);
        }
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
        
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
                
        return jwtDecoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        // 添加以下两行配置
        converter.setAuthorityPrefix(""); // 移除默认的SCOPE_前缀
        converter.setAuthoritiesClaimName("roles"); // 指定使用JWT中的roles声明
        
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/oauth2/token").permitAll() // 允许令牌端点匿名访问
                .requestMatchers("/api/equipments/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SBGL")
                .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_ADMIN","ROLE_CLIENT")
                .requestMatchers("/api/roles/**").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers("/api/permissions/**").hasAnyAuthority("ROLE_ADMIN")               
                .requestMatchers("/api/clients/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            )
            // 添加OAuth2客户端配置
            .oauth2Client(oauth2 -> oauth2
                .clientRegistrationRepository(clientRegistrationRepository(repository))
            );
            
        return http.build();
    }

    // 替换原有的InMemoryClientRegistrationRepository
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(JdbcClientRegistrationRepository repository) {
        return repository;
    }
}

