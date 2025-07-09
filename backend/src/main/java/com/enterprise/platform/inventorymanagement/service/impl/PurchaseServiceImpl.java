package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.PurchaseScanDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Podetails;
import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Pomain;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.HYBarCodeMainRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.PO_PomainRepository;
import com.enterprise.platform.inventorymanagement.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private HYBarCodeMainRepository hyBarCodeMainRepository;

    @Autowired
    private PO_PomainRepository poPomainRepository;

    @Override    
    public PurchaseScanDTO scanPurchaseIn(String barcode) {
        // 1. 根据条码查询条码信息
        Optional<HYBarCodeMain> barCodeOptional = hyBarCodeMainRepository.findByBarcode(barcode);
        if (!barCodeOptional.isPresent()) {
            throw new RuntimeException("条码不存在");
        }

        HYBarCodeMain barCodeMain = barCodeOptional.get();


        // 3. 根据csrccode和Csrcsubid查询采购订单明细
        // 4. 查询采购订单主表信息
        PO_Pomain poPomain = poPomainRepository.findBycPOID(barCodeMain.getCsrccode());
        if (poPomain == null) {
            throw new RuntimeException("未找到对应的采购订单");
        }

        // 5. 查询采购订单子表信息
        Integer cSrcSubID = barCodeMain.getCsrcsubid();
        PO_Podetails poPodetails = poPomain.getPoPodetailsList().stream()
                .filter(item -> item.getId().equals(cSrcSubID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未找到对应的采购订单子表记录"));
        if (poPodetails == null) {
            throw new RuntimeException("未找到对应的采购订单明细");
        }

        // 6. 执行入库逻辑（此处省略具体业务逻辑）
        // 实现实际入库操作

        // 8. 构建返回结果
        PurchaseScanDTO dto = new PurchaseScanDTO();
        dto.setcPOID(poPomain.getcPOID());
        dto.setcInvCode(poPodetails.getcInvCode());
        dto.setiQuantity(poPodetails.getiQuantity());
        // 计算未入库量 = 采购订单数量 - 累计到货数量
        BigDecimal receivedQty = poPodetails.getiReceivedQTY() != null ? poPodetails.getiReceivedQTY() : BigDecimal.ZERO;
        BigDecimal remainingQty = poPodetails.getiQuantity().subtract(receivedQty);
        dto.setRemainingQuantity(remainingQty);
        dto.setBarcode(barCodeMain.getBarcode());

        return dto;
    }

    @Override
    public PurchaseScanDTO getPurchaseOrderByCode(String cPOID) {
        // 1. 查询采购订单主表信息
        PO_Pomain poPomain = poPomainRepository.findBycPOID(cPOID);
        if (poPomain == null) {
            throw new RuntimeException("未找到对应的采购订单");
        }

        // 2. 构建返回结果
        PurchaseScanDTO dto = new PurchaseScanDTO();
        dto.setcPOID(poPomain.getcPOID());
        dto.setdPODate(poPomain.getdPODate());        

        return dto;
    }
}