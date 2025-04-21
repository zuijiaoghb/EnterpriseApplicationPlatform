package com.enterprise.platform.system.service;

import com.enterprise.platform.system.model.OAuth2Client;
import com.enterprise.platform.system.repository.OAuth2ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OAuth2ClientService {
    
    private final OAuth2ClientRepository repository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2ClientService(OAuth2ClientRepository repository, 
                             PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public OAuth2Client createClient(OAuth2Client client) {
        if (repository.existsById(client.getClientId())) {
            throw new IllegalArgumentException("客户端ID已存在");
        }
        
        client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
        client.setCreatedAt(LocalDateTime.now());
        return repository.save(client);
    }

    public List<OAuth2Client> listClients() {
        return repository.findByActiveTrue();
    }

    public void disableClient(String clientId) {
        repository.updateActiveStatus(clientId, false);
    }
}