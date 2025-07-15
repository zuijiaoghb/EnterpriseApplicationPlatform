package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.dto.PurchaseScanDTO;
import com.enterprise.platform.inventorymanagement.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    /**
     * 扫码采购入库
     * @param barcode 条码
     * @return 入库结果
     */
    @GetMapping("/scan-in")
    public ResponseEntity<PurchaseScanDTO> scanPurchaseIn(@RequestParam String barcode) {
        return ResponseEntity.ok(purchaseService.scanPurchaseIn(barcode));
    }

    /**
     * 根据订单号查询采购订单详情
     * @param cPOID 订单号
     * @return 订单详情
     */
    @GetMapping("/order/{cPOID}")
    public ResponseEntity<PurchaseScanDTO> getPurchaseOrderByCode(@PathVariable String cPOID) {
        return ResponseEntity.ok(purchaseService.getPurchaseOrderByCode(cPOID));
    }

    @Operation(summary = "获取供应商已审核的采购订单列表")
    @GetMapping("/supplier/audited-orders")
    public ResponseEntity<List<PurchaseScanDTO>> getSupplierAuditedOrders(
            @RequestParam String supplierCode) {
        List<PurchaseScanDTO> orders = purchaseService.getSupplierAuditedOrders(supplierCode);
        return ResponseEntity.ok(orders);
    }
}