package com.enterprise.platform.system.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "oauth2_client")
public class OAuth2Client {
    @Id
    @Column(name = "client_id", length = 50)
    private String clientId;
    
    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;
    
    @Column(name = "client_secret", nullable = false, length = 100)
    private String clientSecret;
    
    @Column(length = 255)
    private String description;
    
    @Column(nullable = false, length = 200)
    private String scopes;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;
    
    @Column(name = "created_at", updatable = false, 
           columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", 
           columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}