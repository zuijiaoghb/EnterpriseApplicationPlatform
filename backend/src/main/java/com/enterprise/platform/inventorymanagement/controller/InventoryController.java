package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.dto.InventoryDTO;
import com.enterprise.platform.inventorymanagement.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventories() {
        List<InventoryDTO> inventories = inventoryService.getAllInventories();
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }

    @GetMapping("/{cInvCode}")
    public ResponseEntity<InventoryDTO> getInventoryByCode(@PathVariable String cInvCode) {
        Optional<InventoryDTO> inventory = inventoryService.getInventoryByCode(cInvCode);
        return inventory.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/name/{cInvName}")
    public ResponseEntity<List<InventoryDTO>> getInventoriesByName(@PathVariable String cInvName) {
        List<InventoryDTO> inventories = inventoryService.getInventoriesByName(cInvName);
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }
}