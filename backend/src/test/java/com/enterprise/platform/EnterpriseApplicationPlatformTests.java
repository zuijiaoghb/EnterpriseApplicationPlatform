package com.enterprise.platform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.enterprise.platform.equipment.repository.EquipmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = EnterpriseApplicationPlatform.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "logging.config=classpath:logback-test.xml",  // 使用测试专用的日志配置
    "logging.level.root=INFO",
    "spring.jpa.hibernate.ddl-auto=validate"
})
public class EnterpriseApplicationPlatformTests {
    @Autowired
    private EquipmentRepository repository;

    @Test
    void contextLoads() {        
        Assertions.assertNotNull(repository);
    }
}
