package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.dto.PageResultDTO;
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

    /**
     * 根据供应商编码查询已审核的采购订单
     * @param vendorCode 供应商编码
     * @param cPOID 订单号
     * @param dPODate 订单日期
     * @param cInvCode 存货编码
     * @param cItemName 存货名称
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 采购订单列表
     */
    PageResultDTO<PurchaseScanDTO> getVendorAuditedOrders(String vendorCode, String cPOID, String dPODate, String cInvCode, String cItemName, Integer pageNum, Integer pageSize);
}