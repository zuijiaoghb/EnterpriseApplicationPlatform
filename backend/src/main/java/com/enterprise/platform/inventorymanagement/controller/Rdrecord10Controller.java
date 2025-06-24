package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10;
import com.enterprise.platform.inventorymanagement.service.Rdrecord10Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/products")
@RequiredArgsConstructor
public class Rdrecord10Controller {

    private final Rdrecord10Service rdrecord10Service;

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Rdrecord10> getInboundByBarcode(@PathVariable String barcode) {
        return rdrecord10Service.getInboundByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rdrecord10> createInbound(@RequestBody Rdrecord10 inbound) {
        Rdrecord10 created = rdrecord10Service.createInbound(inbound);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/barcode/generate/{barcode}")
    public ResponseEntity<Rdrecord10> generateInboundByBarcode(@PathVariable String barcode, @RequestParam String cwhcode, @RequestParam Integer iQuantity) {
        try {
            Rdrecord10 inbound = rdrecord10Service.createInboundByBarcode(barcode, cwhcode, iQuantity);
            return ResponseEntity.ok(inbound);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
}