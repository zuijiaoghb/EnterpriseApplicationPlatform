package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.dto.CurrentStockDTO;
import com.enterprise.platform.inventorymanagement.service.CurrentStockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/current-stock")
@Tag(name = "当前库存管理")
public class CurrentStockController {

    private final CurrentStockService currentStockService;

    @Autowired
    public CurrentStockController(CurrentStockService currentStockService) {
        this.currentStockService = currentStockService;
    }

    @GetMapping("/inv-code/{cInvCode}")
    @Operation(summary = "根据存货编码查询库存")
    public ResponseEntity<List<CurrentStockDTO>> findByCInvCode(@PathVariable String cInvCode) {
        return ResponseEntity.ok(currentStockService.findByCInvCode(cInvCode));
    }

    @GetMapping("/wh-code/{cWhCode}")
    @Operation(summary = "根据仓库编码查询库存")
    public ResponseEntity<List<CurrentStockDTO>> findByCWhCode(@PathVariable String cWhCode) {
        return ResponseEntity.ok(currentStockService.findByCWhCode(cWhCode));
    }

    @GetMapping("/inv-wh/{cInvCode}/{cWhCode}")
    @Operation(summary = "根据存货编码和仓库编码查询库存")
    public ResponseEntity<List<CurrentStockDTO>> findByCInvCodeAndCWhCode(
            @PathVariable String cInvCode,
            @PathVariable String cWhCode) {
        return ResponseEntity.ok(currentStockService.findByCInvCodeAndCWhCode(cInvCode, cWhCode));
    }
}