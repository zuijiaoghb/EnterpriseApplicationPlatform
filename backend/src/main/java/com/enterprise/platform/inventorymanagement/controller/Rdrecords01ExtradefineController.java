package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords01Extradefine;
import com.enterprise.platform.inventorymanagement.service.Rdrecords01ExtradefineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/rdrecords01-extradefine")
@RequiredArgsConstructor
public class Rdrecords01ExtradefineController {

    private final Rdrecords01ExtradefineService service;

    @GetMapping
    public ResponseEntity<List<Rdrecords01Extradefine>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rdrecords01Extradefine> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rdrecords01Extradefine> create(@RequestBody Rdrecords01Extradefine record) {
        return new ResponseEntity<>(service.save(record), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rdrecords01Extradefine> update(@PathVariable Long id, @RequestBody Rdrecords01Extradefine record) {
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