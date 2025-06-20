package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecord01;
import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecords01;
import java.util.List;

public interface RdRecordService {
    /**
     * 根据采购订单号查询入库单
     */
    RdRecord01 getInboundByPOCode(String poCode);

    /**
     * 扫码创建采购入库单
     */
    RdRecord01 createInboundByBarcode(String barcode, String warehouseCode, Integer quantity);

    /**
     * 获取入库单明细
     */
    List<RdRecords01> getInboundDetails(Long inboundId);

    /**
     * 确认入库单
     */
    RdRecord01 confirmInbound(Long inboundId);
}