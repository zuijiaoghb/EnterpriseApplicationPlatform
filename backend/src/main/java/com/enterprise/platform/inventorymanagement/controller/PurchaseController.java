package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.dto.PurchaseScanDTO;
import com.enterprise.platform.inventorymanagement.model.vo.ResultVO;
import com.enterprise.platform.inventorymanagement.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResultVO<PurchaseScanDTO> scanPurchaseIn(@RequestParam String barcode) {
        return purchaseService.scanPurchaseIn(barcode);
    }

    /**
     * 根据订单号查询采购订单详情
     * @param cPOID 订单号
     * @return 订单详情
     */
    @GetMapping("/order/{cPOID}")
    public ResultVO<PurchaseScanDTO> getPurchaseOrderByCode(@PathVariable String cPOID) {
        return purchaseService.getPurchaseOrderByCode(cPOID);
    }
}