package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10;
import com.enterprise.platform.inventorymanagement.service.Rdrecord10Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/inventory/products")
@RequiredArgsConstructor
@Tag(name = "产成品入库管理", description = "产成品入库单的查询、创建和扫码生成操作")
public class Rdrecord10Controller {

    private final Rdrecord10Service rdrecord10Service;

    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "根据条码查询入库单", description = "通过条码获取对应的产成品入库单信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(schema = @Schema(implementation = Rdrecord10.class))),
        @ApiResponse(responseCode = "404", description = "入库单不存在")
    })
    @Parameter(name = "barcode", description = "产成品条码", required = true)
    public ResponseEntity<Rdrecord10> getInboundByBarcode(@PathVariable String barcode) {
        return rdrecord10Service.getInboundByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "创建入库单", description = "手动创建新的产成品入库单")
    @ApiResponse(responseCode = "200", description = "创建成功", content = @Content(schema = @Schema(implementation = Rdrecord10.class)))
    @Parameter(name = "inbound", description = "入库单信息", required = true)
    public ResponseEntity<Rdrecord10> createInbound(@RequestBody Rdrecord10 inbound) {
        Rdrecord10 created = rdrecord10Service.createInbound(inbound);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/barcode/generate/{barcode}")
    @Operation(summary = "通过条码生成入库单", description = "扫描条码并传入仓库编码和数量，自动生成产成品入库单")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "生成成功", content = @Content(schema = @Schema(implementation = Rdrecord10.class))),
        @ApiResponse(responseCode = "400", description = "参数错误或生成失败")
    })
    @Parameter(name = "barcode", description = "产成品条码", required = true)
    @Parameter(name = "cwhcode", description = "仓库编码", required = true)
    @Parameter(name = "iQuantity", description = "入库数量", required = true)
    public ResponseEntity<Rdrecord10> generateInboundByBarcode(@PathVariable String barcode, @RequestParam String cwhcode, @RequestParam Integer iQuantity) {
        try {
            Rdrecord10 inbound = rdrecord10Service.createInboundByBarcode(barcode, cwhcode, iQuantity);
            return ResponseEntity.ok(inbound);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
}