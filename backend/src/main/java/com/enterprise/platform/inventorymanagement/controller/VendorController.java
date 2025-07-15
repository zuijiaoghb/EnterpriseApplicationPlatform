package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.dto.VendorDTO;
import com.enterprise.platform.inventorymanagement.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/vendors")
@RequiredArgsConstructor
@Tag(name = "供应商管理", description = "供应商相关操作接口")
public class VendorController {

    private final VendorService vendorService;

    @Operation(summary = "获取所有供应商")
    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.findAll());
    }

    @Operation(summary = "根据编码获取供应商")
    @GetMapping("/{cVenCode}")
    public ResponseEntity<VendorDTO> getVendorById(@PathVariable String cVenCode) {
        return ResponseEntity.ok(vendorService.findById(cVenCode));
    }

    @Operation(summary = "创建新供应商")
    @PostMapping
    public ResponseEntity<VendorDTO> createVendor(@RequestBody VendorDTO vendorDTO) {
        VendorDTO savedVendor = vendorService.save(vendorDTO);
        return ResponseEntity.status(201).body(savedVendor);
    }

    @Operation(summary = "更新供应商信息")
    @PutMapping("/{cVenCode}")
    public ResponseEntity<VendorDTO> updateVendor(
            @PathVariable String cVenCode,
            @RequestBody VendorDTO vendorDTO) {
        return ResponseEntity.ok(vendorService.update(cVenCode, vendorDTO));
    }

    @Operation(summary = "删除供应商")
    @DeleteMapping("/{cVenCode}")
    public ResponseEntity<Void> deleteVendor(@PathVariable String cVenCode) {
        vendorService.deleteById(cVenCode);
        return ResponseEntity.noContent().build();
    }
}