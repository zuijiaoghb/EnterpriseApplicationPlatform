package com.enterprise.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@SpringBootApplication
@EntityScan(basePackages = {
    "com.enterprise.platform.equipment.model",
    "com.enterprise.platform.user.model",  // Add user model package
    "com.enterprise.platform.system.model"
})
public class EnterpriseApplicationPlatform {
    public static void main(String[] args) {       
        ConfigurableApplicationContext ctx = SpringApplication.run(EnterpriseApplicationPlatform.class, args);
        
        // 验证JWT编码器配置
        JwtEncoder encoder = ctx.getBean(JwtEncoder.class);
        if (encoder instanceof NimbusJwtEncoder) {
            System.out.println("✅ 正确的JWT编码器配置: NimbusJwtEncoder");
        } else {
            System.err.println("❌ 错误的JWT编码器类型: " + encoder.getClass().getName());
        }
    }
}
