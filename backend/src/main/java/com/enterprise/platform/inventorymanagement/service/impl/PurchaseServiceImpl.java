package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.PurchaseScanDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Inventory;
import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Podetails;
import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Pomain;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.HYBarCodeMainRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.InventoryRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.PO_PodetailsRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.PO_PomainRepository;
import com.enterprise.platform.inventorymanagement.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Vendor;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.VendorRepository;
import com.enterprise.platform.inventorymanagement.model.sqlserver.ComputationUnit;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.ComputationUnitRepository;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private HYBarCodeMainRepository hyBarCodeMainRepository;

    @Autowired
    private PO_PomainRepository poPomainRepository;

    @Autowired
    private PO_PodetailsRepository poPodetailsRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ComputationUnitRepository computationUnitRepository;

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

    @Override
    public List<PurchaseScanDTO> getVendorAuditedOrders(String vendorCode) {
        // 查询供应商已审核的采购订单
        List<PO_Pomain> pomainList = poPomainRepository.findByCVenCodeAndCAuditDateIsNotNull(vendorCode);

        return pomainList.stream().flatMap(pomain -> {
            // 查询订单明细
            List<PO_Podetails> podetailsList = poPodetailsRepository.findByPoPomainPoid(pomain.getPoid());

            // 查询供应商名称
            Vendor vendor = vendorRepository.findById(pomain.getcVenCode())
                .orElseThrow(() -> new RuntimeException("供应商不存在: " + pomain.getcVenCode()));
            String supplierName = vendor.getCVenName();

            return podetailsList.stream().map(podetails -> {
                PurchaseScanDTO dto = new PurchaseScanDTO();
                // 设置主表信息
                dto.setcPOID(pomain.getcPOID());
                dto.setdPODate(pomain.getdPODate());
                dto.setcVenCode(pomain.getcVenCode());
                dto.setcDefine1(pomain.getcDefine1());
                dto.setcPersonCode(pomain.getcPersonCode());
                dto.setcDepCode(pomain.getcDepCode());
                dto.setdArriveDate(podetails.getdArriveDate());

                // 设置明细信息
                dto.setcInvCode(podetails.getcInvCode());
                dto.setiQuantity(podetails.getiQuantity());
                dto.setIrowno(podetails.getIvouchrowno());
                // 使用iNum作为一箱的数量
                dto.setBoxQuantity(podetails.getiNum());
                dto.setcUnitID(podetails.getcUnitID());

                // 查询存货名称
                Inventory inventory = inventoryRepository.findById(podetails.getcInvCode())
                    .orElseThrow(() -> new RuntimeException("存货不存在: " + podetails.getcInvCode()));
                dto.setcItemName(inventory.getCInvName());

                // 查询单位名称
                ComputationUnit computationUnit = computationUnitRepository.findById(inventory.getCComUnitCode())
                    .orElseThrow(() -> new RuntimeException("单位不存在: " + inventory.getCComUnitCode()));
                dto.setUnitName(computationUnit.getCComUnitName());

                // 生成批号(当前日期+时分秒)
                String batchNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                dto.setBatchNumber(batchNumber);

                // 生成条码
                String barcode = String.format("%s_%s_%s_%s_%d_%s",
                    pomain.getcVenCode(),
                    podetails.getcInvCode(),
                    dto.getBoxQuantity(),
                    pomain.getcPOID(),
                    podetails.getIvouchrowno(),
                    batchNumber);
                dto.setBarcode(barcode);
                dto.setSupplierName(supplierName);

                return dto;
            });
        }).collect(Collectors.toList());
    }
}