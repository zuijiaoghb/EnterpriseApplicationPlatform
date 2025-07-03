package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords10Extradefine;
import com.enterprise.platform.inventorymanagement.service.Rdrecords10ExtradefineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory/rdrecords10-extradefine")
@RequiredArgsConstructor
public class Rdrecords10ExtradefineController {
    private final Rdrecords10ExtradefineService service;

    @GetMapping
    public ResponseEntity<List<Rdrecords10Extradefine>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rdrecords10Extradefine> getById(@PathVariable Long id) {
        Optional<Rdrecords10Extradefine> entity = service.findById(id);
        return entity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rdrecords10Extradefine> create(@RequestBody Rdrecords10Extradefine entity) {
        return new ResponseEntity<>(service.save(entity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rdrecords10Extradefine> update(@PathVariable Long id, @RequestBody Rdrecords10Extradefine entity) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        entity.setAutoID(id);
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