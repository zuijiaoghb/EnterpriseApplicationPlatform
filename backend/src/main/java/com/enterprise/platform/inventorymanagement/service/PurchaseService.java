package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.dto.PurchaseScanDTO;

public interface PurchaseService {
    /**
     * 扫码采购入库
     * @param barcode 条码
     * @return 入库结果
     */
    PurchaseScanDTO scanPurchaseIn(String barcode);

    /**
     * 根据订单号查询采购订单详情
     * @param cPOID 订单号
     * @return 订单详情
     */
    PurchaseScanDTO getPurchaseOrderByCode(String cPOID);
}