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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
        // 转换dPODate格式: 从Tue,+03+Jun+2025+16:00:00+GMT转换为YYYY-MM-DD
        String formattedDPODate = null;
        if (dPODate != null && !dPODate.isEmpty()) {
            try {
                // 解析GMT格式日期
                SimpleDateFormat gmtFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                Date date = gmtFormat.parse(dPODate);
                // 格式化为YYYY-MM-DD
                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                formattedDPODate = targetFormat.format(date);
                log.info("Converted dPODate from {} to {}", dPODate, formattedDPODate);
            } catch (ParseException e) {
                log.error("Failed to parse dPODate: {}", dPODate, e);
                // 如果解析失败，使用原始值
                formattedDPODate = dPODate;
            }
        }

        // 调用Repository层方法获取所有符合条件的记录
        List<PO_Pomain> allPoPomainList = poPomainRepository.findAllByCVenCodeAndCAuditDateIsNotNullAndCPOIDLikeAndDPODateLikeAndCInvCodeLikeAndCItemNameLike(
            vendorCode, cPOID, formattedDPODate, cInvCode, cItemName);        

        try {
            // 使用for循环逐行打印所有数据的属性值
            log.info("getVendorAuditedOrders, allPoPomainList size: {}", allPoPomainList.size());
        } catch (Exception e) {
            log.error("Failed to print allPoPomainList", e);
        }
        
        // 转换为DTO
        List<PurchaseScanDTO> allDtos = convertToPurchaseScanDTOs(allPoPomainList);
        
        // 如果提供了cInvCode，过滤只包含该存货编码的DTO
        if (cInvCode != null && !cInvCode.isEmpty()) {
            allDtos = allDtos.stream()
                    .filter(dto -> dto.getcInvCode() != null && dto.getcInvCode().contains(cInvCode))
                    .collect(Collectors.toList());
        }

        // 如果提供了cItemName，过滤只包含该存货名称的DTO
        if (cItemName != null && !cItemName.isEmpty()) {
            allDtos = allDtos.stream()
                    .filter(dto -> dto.getcItemName() != null && dto.getcItemName().contains(cItemName))
                    .collect(Collectors.toList());
        }
        
        log.info("getVendorAuditedOrders, allDtos size: {}", allDtos.size());

        // 计算分页参数
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, allDtos.size());

        // 执行应用层分页
        List<PurchaseScanDTO> pagedDtos;
        if (startIndex >= allDtos.size()) {
            pagedDtos = Collections.emptyList();
        } else {
            pagedDtos = allDtos.subList(startIndex, endIndex);
        }

        // 获取总记录数
        long total = allDtos.size();

        log.info("getVendorAuditedOrders, total: {}, pagedDtos: {}", total, pagedDtos);
        return new PageResultDTO<PurchaseScanDTO>(total, pagedDtos);
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
                .filter(podetails -> !Objects.equals(podetails.getiQuantity(), podetails.getiReceivedQTY()))
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