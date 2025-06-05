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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional(readOnly = true, transactionManager = "mysqlTransactionManager")
public class OAuth2ClientService {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2ClientService.class);
    
    private final OAuth2ClientRepository repository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2ClientService(OAuth2ClientRepository repository, 
                             PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(transactionManager = "mysqlTransactionManager")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OAuth2Client createClient(OAuth2Client client) {
        if (repository.existsById(client.getClientId())) {
            throw new DuplicateClientException("客户端ID已存在");
        }
        
        // 自动生成随机密钥
        String rawSecret = generateRandomSecret();
        client.setClientSecret(passwordEncoder.encode(rawSecret));
        client.setCreatedAt(LocalDateTime.now());
        client.setActive(true);
        
        OAuth2Client saved = repository.save(client);
        logger.info("Client created: {}", saved.getClientId());
        // 返回包含原始密钥的对象
        return saved.setRawClientSecret(rawSecret);
    }

    public List<OAuth2Client> listActiveClients() {
        return repository.findByActiveTrue();
    }

    @Transactional(transactionManager = "mysqlTransactionManager")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void disableClient(String clientId) {
        OAuth2Client client = repository.findById(clientId)
            .orElseThrow(() -> new ClientNotFoundException(clientId));
        
        // 修改为直接设置状态并保存实体
        client.setActive(false);
        repository.save(client);
    }

    @Transactional(transactionManager = "mysqlTransactionManager")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OAuth2Client updateClient(OAuth2Client client) {
        OAuth2Client existing = repository.findById(client.getClientId())
            .orElseThrow(() -> new ClientNotFoundException(client.getClientId()));
        
        existing.setClientName(client.getClientName());
        existing.setScopes(client.getScopes());
        
        return repository.save(existing);
    }

    @Transactional(transactionManager = "mysqlTransactionManager")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OAuth2Client resetClientSecret(String clientId) {
        OAuth2Client client = repository.findById(clientId)
            .orElseThrow(() -> new ClientNotFoundException(clientId));
        
        // 生成新密钥并加密
        String newSecret = generateRandomSecret();
        client.setClientSecret(passwordEncoder.encode(newSecret));
        client.setUpdatedAt(LocalDateTime.now());
        
        OAuth2Client saved = repository.save(client);
        // 返回包含原始密钥的对象
        return saved.setRawClientSecret(newSecret);
    }

    private String generateRandomSecret() {
        // 实现随机密钥生成逻辑
        return UUID.randomUUID().toString().replace("-", "");
    }

    public List<OAuth2Client> listAllClients() {
        return repository.findAllClients();
    }

    @Transactional(transactionManager = "mysqlTransactionManager")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OAuth2Client enableClient(String clientId) {
        OAuth2Client client = repository.findById(clientId)
            .orElseThrow(() -> new ClientNotFoundException(clientId));
        
        client.setActive(true);
        return repository.save(client);
    }
    
    public OAuth2Client getClientById(String clientId) {
        return repository.findById(clientId)
        .orElseThrow(() -> new ClientNotFoundException(clientId));
    }
    
    public boolean clientIdExists(String clientId) {
        return repository.existsById(clientId);
    }
}