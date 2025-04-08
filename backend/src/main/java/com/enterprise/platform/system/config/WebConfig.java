package com.enterprise.platform.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://192.168.21.175:3000")
            .allowedMethods("*")
            .allowedHeaders("*")
            .exposedHeaders("Authorization")  // 关键：暴露认证头
            .allowCredentials(true)
            .maxAge(3600);
    }
}
