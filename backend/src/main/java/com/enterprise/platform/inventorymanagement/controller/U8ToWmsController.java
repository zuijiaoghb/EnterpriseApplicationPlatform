package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.dto.U8ToWmsDTO;
import com.enterprise.platform.inventorymanagement.service.U8ToWmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/u8towms")
@RequiredArgsConstructor
public class U8ToWmsController {

    private final U8ToWmsService u8ToWmsService;

    @GetMapping("/{id}")
    public ResponseEntity<U8ToWmsDTO> getById(@PathVariable Long id) {
        return u8ToWmsService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<U8ToWmsDTO>> getAll() {
        return ResponseEntity.ok(u8ToWmsService.getAll());
    }

    @PostMapping
    public ResponseEntity<U8ToWmsDTO> create(@RequestBody U8ToWmsDTO u8ToWmsDTO) {
        U8ToWmsDTO savedDTO = u8ToWmsService.save(u8ToWmsDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDTO.getAID())
                .toUri();
        return ResponseEntity.created(location).body(savedDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<U8ToWmsDTO> update(@PathVariable Long id, @RequestBody U8ToWmsDTO u8ToWmsDTO) {
        return ResponseEntity.ok(u8ToWmsService.update(id, u8ToWmsDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        u8ToWmsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}