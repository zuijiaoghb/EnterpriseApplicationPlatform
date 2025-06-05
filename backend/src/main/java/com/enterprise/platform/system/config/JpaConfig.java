package com.enterprise.platform.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;

@Configuration
public class JpaConfig {
    
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return props -> {
            props.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
            props.put("hibernate.jdbc.time_zone", "UTC");            
        };
    }
}
