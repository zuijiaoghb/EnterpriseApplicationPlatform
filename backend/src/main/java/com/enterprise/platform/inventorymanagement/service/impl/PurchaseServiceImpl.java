package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.PageResultDTO;
import com.enterprise.platform.inventorymanagement.model.dto.PurchaseScanDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Inventory;
import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Podetails;
import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Pomain;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.HYBarCodeMainRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.InventoryRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.PO_PomainRepository;
import com.enterprise.platform.inventorymanagement.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Vendor;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.VendorRepository;
import com.enterprise.platform.inventorymanagement.model.sqlserver.ComputationUnit;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.ComputationUnitRepository;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class PurchaseServiceImpl implements PurchaseService {
    private static final Logger log = LoggerFactory.getLogger(PurchaseServiceImpl.class);


    @Autowired
    private HYBarCodeMainRepository hyBarCodeMainRepository;

    @Autowired
    private PO_PomainRepository poPomainRepository;

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
    public PageResultDTO<PurchaseScanDTO> getVendorAuditedOrders(String vendorCode, String cPOID, String dPODate, String cInvCode, String cItemName, Integer pageNum, Integer pageSize) {
        // 计算offset并检查边界
        long offset = (long)(pageNum - 1) * pageSize;
        if (offset > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Offset exceeds maximum integer value");
        }
        // 调用Repository层方法，传递新的搜索参数
        List<PO_Pomain> poPomainList = poPomainRepository.findByCVenCodeAndCAuditDateIsNotNullAndCPOIDLikeAndDPODateLikeAndCInvCodeLikeAndCItemNameLike(
            vendorCode, cPOID, dPODate, cInvCode, cItemName, (int)offset, pageSize);
        long total = poPomainRepository.countByCVenCodeAndCAuditDateIsNotNullAndCPOIDLikeAndDPODateLikeAndCInvCodeLikeAndCItemNameLike(
            vendorCode, cPOID, dPODate, cInvCode, cItemName);
        // 转换为DTO并返回分页结果
        List<PurchaseScanDTO> dtos = convertToPurchaseScanDTOs(poPomainList);
        return new PageResultDTO<PurchaseScanDTO>(total, dtos);
    }

    /**
     * 将采购订单转换为DTO列表
     */
    private List<PurchaseScanDTO> convertToPurchaseScanDTOs(List<PO_Pomain> poPomainList) {
        if (poPomainList.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Extract all vendor codes
        Set<String> venCodeSet = poPomainList.stream()
                .map(PO_Pomain::getcVenCode)
                .collect(Collectors.toSet());
        Map<String, Vendor> vendorMap = vendorRepository.findByCVenCodeIn(venCodeSet).stream()
                .collect(Collectors.toMap(Vendor::getCVenCode, vendor -> vendor, (existing, replacement) -> existing));
        
        // Extract all inventory codes
        Set<String> invCodeSet = poPomainList.stream()
                .flatMap(pomain -> pomain.getPoPodetailsList().stream())
                .map(PO_Podetails::getcInvCode)
                .collect(Collectors.toSet());
        Map<String, Inventory> inventoryMap = inventoryRepository.findByCInvCodeIn(invCodeSet).stream()
                .collect(Collectors.toMap(Inventory::getCInvCode, inventory -> inventory, (existing, replacement) -> existing));
        
        // Extract all unit codes
        Set<String> unitCodeSet = inventoryMap.values().stream()
                .map(Inventory::getCComUnitCode)
                .collect(Collectors.toSet());
        Map<String, ComputationUnit> unitMap = computationUnitRepository.findByCComunitCodeIn(unitCodeSet).stream()
                .collect(Collectors.toMap(ComputationUnit::getCComunitCode, unit -> unit, (existing, replacement) -> existing));
        
        // Process each PO_Pomain
        return poPomainList.stream()
                .flatMap(pomain -> convertToPurchaseScanDTOs(pomain, vendorMap, inventoryMap, unitMap))
                .collect(Collectors.toList());
    }

    private Stream<PurchaseScanDTO> convertToPurchaseScanDTOs(PO_Pomain pomain, Map<String, Vendor> vendorMap, 
                                                             Map<String, Inventory> inventoryMap, Map<String, ComputationUnit> unitMap) {
        // 查询订单明细
        // 使用已加载的明细列表，避免N+1查询
        List<PO_Podetails> podetailsList = pomain.getPoPodetailsList();
        log.debug("订单[{}]包含[{}]条明细记录", pomain.getcPOID(), podetailsList.size());

        // 获取供应商名称
        Vendor vendor = vendorMap.get(pomain.getcVenCode());
        if (vendor == null) {
            log.error("供应商不存在: {}", pomain.getcVenCode());
            return Stream.empty();
        }
        String supplierName = vendor.getCVenName();

        return podetailsList.stream()
                .filter(Objects::nonNull)
                .filter(podetails -> !podetails.getiQuantity().equals(podetails.getiReceivedQTY()))
                .map(podetails -> buildPurchaseScanDTO(pomain, podetails, supplierName, inventoryMap, unitMap));
    }

    /**
     * 构建采购订单DTO对象
     */
    private PurchaseScanDTO buildPurchaseScanDTO(PO_Pomain pomain, PO_Podetails podetails, String supplierName, 
                                                Map<String, Inventory> inventoryMap, Map<String, ComputationUnit> unitMap) {
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
        dto.setBoxQuantity(podetails.getiNum());
        dto.setcUnitID(podetails.getcUnitID());

        // 设置存货名称
        Inventory inventory = inventoryMap.get(podetails.getcInvCode());
        if (inventory != null) {
            dto.setcItemName(inventory.getCInvName());
            // 设置单位名称
            ComputationUnit unit = unitMap.get(inventory.getCComUnitCode());
            if (unit != null) {
                dto.setUnitName(unit.getCComUnitName());
            } else {
                log.warn("单位不存在: {}", inventory.getCComUnitCode());
                dto.setUnitName("未知单位");
            }
        } else {
            log.warn("存货不存在: {}", podetails.getcInvCode());
            dto.setcItemName("未知存货");
            dto.setUnitName("未知单位");
        }

        // 生成批号和条码
        String batchNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        dto.setBatchNumber(batchNumber);
        dto.setBarcode(String.format("%s_%s_%s_%s_%d_%s",
                pomain.getcVenCode(), podetails.getcInvCode(), dto.getBoxQuantity(),
                pomain.getcPOID(), podetails.getIvouchrowno(), batchNumber));
        dto.setSupplierName(supplierName);

        // 计算已入库数量和剩余未入库数量
        BigDecimal receivedQty = podetails.getiReceivedQTY() != null ? podetails.getiReceivedQTY() : BigDecimal.ZERO;
        dto.setReceivedQuantity(receivedQty);
        BigDecimal remainingQuantity = podetails.getiQuantity().subtract(receivedQty);
        dto.setRemainingQuantity(remainingQuantity);

        return dto;
    }
}