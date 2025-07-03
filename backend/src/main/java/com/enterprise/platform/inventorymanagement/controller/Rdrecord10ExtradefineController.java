package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10Extradefine;
import com.enterprise.platform.inventorymanagement.service.Rdrecord10ExtradefineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory/rdrecord10-extradefine")
@RequiredArgsConstructor
public class Rdrecord10ExtradefineController {
    private final Rdrecord10ExtradefineService service;

    @GetMapping
    public ResponseEntity<List<Rdrecord10Extradefine>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rdrecord10Extradefine> getById(@PathVariable Long id) {
        Optional<Rdrecord10Extradefine> entity = service.findById(id);
        return entity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rdrecord10Extradefine> create(@RequestBody Rdrecord10Extradefine entity) {
        return new ResponseEntity<>(service.save(entity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rdrecord10Extradefine> update(@PathVariable Long id, @RequestBody Rdrecord10Extradefine entity) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        entity.setID(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}