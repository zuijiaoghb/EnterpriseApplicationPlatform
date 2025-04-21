package com.enterprise.platform.user.controller;

import com.enterprise.platform.system.model.OAuth2Client;
import com.enterprise.platform.system.service.OAuth2ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(clientService.createClient(client));
    }
    
    @GetMapping
    public ResponseEntity<List<OAuth2Client>> listClients() {
        return ResponseEntity.ok(clientService.listClients());
    }
    
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> disableClient(@PathVariable String clientId) {
        clientService.disableClient(clientId);
        return ResponseEntity.noContent().build();
    }
}
