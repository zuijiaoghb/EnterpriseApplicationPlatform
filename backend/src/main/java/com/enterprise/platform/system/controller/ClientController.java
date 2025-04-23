package com.enterprise.platform.system.controller;

import com.enterprise.platform.system.model.OAuth2Client;
import com.enterprise.platform.system.service.OAuth2ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ClientController {
    
    private final OAuth2ClientService clientService;

    public ClientController(OAuth2ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<OAuth2Client> createClient(@RequestBody OAuth2Client client) {
        OAuth2Client created = clientService.createClient(client);
        // 返回包含原始密钥的对象
        return ResponseEntity.ok(created.setRawClientSecret(created.getRawClientSecret()));
    }
    
    @GetMapping
    public ResponseEntity<List<OAuth2Client>> listClients() {
        return ResponseEntity.ok(clientService.listActiveClients());
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<OAuth2Client>> listAllClients() {
        return ResponseEntity.ok(clientService.listAllClients());
    }

    @PutMapping("/{clientId}/enable")
    public ResponseEntity<OAuth2Client> enableClient(@PathVariable String clientId) {
        return ResponseEntity.ok(clientService.enableClient(clientId));
    }
    
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> disableClient(@PathVariable String clientId) {
        clientService.disableClient(clientId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<OAuth2Client> updateClient(
            @PathVariable String clientId,
            @RequestBody OAuth2Client client) {
        client.setClientId(clientId);
        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @PutMapping("/{clientId}/reset-secret")
    public ResponseEntity<OAuth2Client> resetClientSecret(
            @PathVariable String clientId) {               
        OAuth2Client updated = clientService.resetClientSecret(clientId);
        // 返回包含新生成的原始密钥的对象
        return ResponseEntity.ok(updated.setRawClientSecret(updated.getRawClientSecret()));
        //return ResponseEntity.ok(clientService.resetClientSecret(clientId));
    }

    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkClientIdExists(
            @RequestParam String clientId) {
        boolean exists = clientService.clientIdExists(clientId);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }
}
