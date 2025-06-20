package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecord01;
import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecords01;
import com.enterprise.platform.inventorymanagement.service.RdRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/inbound")
@RequiredArgsConstructor
@Tag(name = "采购入库管理", description = "扫码采购入库相关操作")
public class RdRecordController {

    private final RdRecordService rdRecordService;

    @GetMapping("/po/{poCode}")
    @Operation(summary = "根据采购订单号查询入库单")
    public ResponseEntity<RdRecord01> getInboundByPOCode(@PathVariable String poCode) {
        return ResponseEntity.ok(rdRecordService.getInboundByPOCode(poCode));
    }

    @PostMapping("/barcode")
    @Operation(summary = "扫码创建采购入库单")
    public ResponseEntity<RdRecord01> createInboundByBarcode(
            @RequestParam String barcode,
            @RequestParam String warehouseCode,
            @RequestParam Integer quantity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rdRecordService.createInboundByBarcode(barcode, warehouseCode, quantity));
    }

    @GetMapping("/{inboundId}/details")
    @Operation(summary = "获取入库单明细")
    public ResponseEntity<List<RdRecords01>> getInboundDetails(@PathVariable Long inboundId) {
        return ResponseEntity.ok(rdRecordService.getInboundDetails(inboundId));
    }

    @PutMapping("/{inboundId}/confirm")
    @Operation(summary = "确认入库单")
    public ResponseEntity<RdRecord01> confirmInbound(@PathVariable Long inboundId) {
        return ResponseEntity.ok(rdRecordService.confirmInbound(inboundId));
    }
}