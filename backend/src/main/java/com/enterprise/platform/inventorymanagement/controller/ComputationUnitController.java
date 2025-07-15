package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.dto.ComputationUnitDTO;
import com.enterprise.platform.inventorymanagement.service.ComputationUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/computation-units")
@RequiredArgsConstructor
@Tag(name = "计量单位管理", description = "计量单位相关操作接口")
public class ComputationUnitController {

    private final ComputationUnitService computationUnitService;

    @Operation(summary = "获取所有计量单位")
    @GetMapping
    public ResponseEntity<List<ComputationUnitDTO>> getAllComputationUnits() {
        return ResponseEntity.ok(computationUnitService.findAll());
    }

    @Operation(summary = "根据编码获取计量单位")
    @GetMapping("/{cComunitCode}")
    public ResponseEntity<ComputationUnitDTO> getComputationUnitById(@PathVariable String cComunitCode) {
        return ResponseEntity.ok(computationUnitService.findById(cComunitCode));
    }

    @Operation(summary = "创建新计量单位")
    @PostMapping
    public ResponseEntity<ComputationUnitDTO> createComputationUnit(@RequestBody ComputationUnitDTO computationUnitDTO) {
        ComputationUnitDTO savedComputationUnit = computationUnitService.save(computationUnitDTO);
        return ResponseEntity.status(201).body(savedComputationUnit);
    }

    @Operation(summary = "更新计量单位信息")
    @PutMapping("/{cComunitCode}")
    public ResponseEntity<ComputationUnitDTO> updateComputationUnit(
            @PathVariable String cComunitCode,
            @RequestBody ComputationUnitDTO computationUnitDTO) {
        return ResponseEntity.ok(computationUnitService.update(cComunitCode, computationUnitDTO));
    }

    @Operation(summary = "删除计量单位")
    @DeleteMapping("/{cComunitCode}")
    public ResponseEntity<Void> deleteComputationUnit(@PathVariable String cComunitCode) {
        computationUnitService.deleteById(cComunitCode);
        return ResponseEntity.noContent().build();
    }
}