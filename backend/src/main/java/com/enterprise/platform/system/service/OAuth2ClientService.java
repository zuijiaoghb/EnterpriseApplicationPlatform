package com.enterprise.platform.system.service;

import com.enterprise.platform.system.exception.ClientNotFoundException;
import com.enterprise.platform.system.exception.DuplicateClientException;
import com.enterprise.platform.system.model.OAuth2Client;
import com.enterprise.platform.system.repository.OAuth2ClientRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OAuth2ClientService {
    
    private final OAuth2ClientRepository repository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2ClientService(OAuth2ClientRepository repository, 
                             PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OAuth2Client createClient(OAuth2Client client) {
        if (repository.existsById(client.getClientId())) {
            throw new DuplicateClientException("客户端ID已存在");
        }
        
        client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
        client.setCreatedAt(LocalDateTime.now());
        client.setActive(true);
        return repository.save(client);
    }

    public List<OAuth2Client> listActiveClients() {
        return repository.findByActiveTrue();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void disableClient(String clientId) {
        OAuth2Client client = repository.findById(clientId)
            .orElseThrow(() -> new ClientNotFoundException(clientId));
        
        // 修改为直接设置状态并保存实体
        client.setActive(false);
        repository.save(client);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OAuth2Client updateClient(OAuth2Client client) {
        OAuth2Client existing = repository.findById(client.getClientId())
            .orElseThrow(() -> new ClientNotFoundException(client.getClientId()));
        
        existing.setClientName(client.getClientName());
        existing.setScopes(client.getScopes());
        
        return repository.save(existing);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OAuth2Client resetClientSecret(String clientId) {
        OAuth2Client client = repository.findById(clientId)
            .orElseThrow(() -> new ClientNotFoundException(clientId));
        
        // 生成新密钥并加密
        String newSecret = generateRandomSecret();
        client.setClientSecret(passwordEncoder.encode(newSecret));
        client.setUpdatedAt(LocalDateTime.now());
        
        OAuth2Client saved = repository.save(client);
        saved.setClientSecret(newSecret); // 返回明文密钥供一次性显示
        
        return saved;
    }

    private String generateRandomSecret() {
        // 实现随机密钥生成逻辑
        return UUID.randomUUID().toString().replace("-", "");
    }
}