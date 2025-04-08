package com.enterprise.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
    "com.enterprise.platform.equipment.model",
    "com.enterprise.platform.user.model"  // Add user model package
})
@EnableJpaRepositories(basePackages = {
    "com.enterprise.platform.equipment.repository",
    "com.enterprise.platform.user.repository"  // Add user repository package
})
public class EnterpriseApplicationPlatform {
    public static void main(String[] args) {
        SpringApplication.run(EnterpriseApplicationPlatform.class, args);
    }
}
