package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecord01Extradefine;
import com.enterprise.platform.inventorymanagement.service.RdRecord01ExtradefineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/rdrecord01-extradefine")
@RequiredArgsConstructor
public class RdRecord01ExtradefineController {

    private final RdRecord01ExtradefineService service;

    @GetMapping
    public ResponseEntity<List<RdRecord01Extradefine>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RdRecord01Extradefine> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RdRecord01Extradefine> create(@RequestBody RdRecord01Extradefine record) {
        return new ResponseEntity<>(service.save(record), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RdRecord01Extradefine> update(@PathVariable Long id, @RequestBody RdRecord01Extradefine record) {
        try {
            return ResponseEntity.ok(service.update(id, record));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}