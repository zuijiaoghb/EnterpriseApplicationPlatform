package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.PageResultDTO;
import com.enterprise.platform.inventorymanagement.model.dto.PurchaseScanDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Podetails;
import com.enterprise.platform.inventorymanagement.model.sqlserver.PO_Pomain;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.HYBarCodeMainRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.PO_PomainRepository;
import com.enterprise.platform.inventorymanagement.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Vendor;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.VendorRepository;
import com.enterprise.platform.inventorymanagement.model.sqlserver.ComputationUnit;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.ComputationUnitRepository;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Person;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.PersonRepository;
import java.util.HashSet;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class PurchaseServiceImpl implements PurchaseService {
    private static final Logger log = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    // 在类中添加HYBarCodeMainRepository的注入
    @Autowired
    private HYBarCodeMainRepository hyBarCodeMainRepository;

    @Autowired
    private PO_PomainRepository poPomainRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ComputationUnitRepository computationUnitRepository;

    @Autowired
    private PersonRepository personRepository;

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
        // 性能优化：使用基于明细表的分页查询，确保分页与实际数据一致
        
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

        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;
        
        long startTime = System.currentTimeMillis();
        
        // 使用基于明细表的分页查询，直接获取明细数据
        List<Object[]> results = poPomainRepository.findDetailsByCVenCodeAndCAuditDateIsNotNullAndCPOIDLikeAndDPODateLikeAndCInvCodeLikeAndCItemNameLike(
            vendorCode, cPOID, formattedDPODate, cInvCode, cItemName, offset, pageSize);
        
        // 获取总数（基于明细表记录数）
        long totalCount = poPomainRepository.countDetailsByCVenCodeAndCAuditDateIsNotNullAndCPOIDLikeAndDPODateLikeAndCInvCodeLikeAndCItemNameLike(
            vendorCode, cPOID, formattedDPODate, cInvCode, cItemName);
        
        long queryTime = System.currentTimeMillis() - startTime;
        log.info("数据库查询完成, 耗时: {}ms, 总数: {}, 当前页: {}, 页大小: {}", 
                 queryTime, totalCount, pageNum, pageSize);

        // 将查询结果直接转换为PurchaseScanDTO
        List<PurchaseScanDTO> dtos = convertObjectsToPurchaseScanDTOs(results);
        
        return new PageResultDTO<PurchaseScanDTO>(totalCount, dtos);
    }

    /**
     * 将Object[]查询结果转换为PurchaseScanDTO列表
     */
    private List<PurchaseScanDTO> convertObjectsToPurchaseScanDTOs(List<Object[]> results) {
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        
        long startTime = System.currentTimeMillis();
        List<PurchaseScanDTO> dtos = new ArrayList<>();
        String batchNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // 收集所有需要的供应商编码、单位编码和人员编码，用于批量查询
        Set<String> venCodeSet = new HashSet<>();
        Set<String> unitCodeSet = new HashSet<>();
        Set<String> personCodeSet = new HashSet<>();
        
        for (Object[] row : results) {
            if (row.length >= 18) {
                Object venCode = row[3];
                if (venCode != null) {
                    venCodeSet.add(venCode.toString());
                }
                Object unitCode = row[17];
                if (unitCode != null) {
                    unitCodeSet.add(unitCode.toString());
                }
                Object personCode = row[7];
                if (personCode != null) {
                    personCodeSet.add(personCode.toString());
                }
            }
        }
        
        // 批量查询供应商、单位和人员信息
        Map<String, Vendor> vendorMap = vendorRepository.findByCVenCodeIn(venCodeSet).stream()
                .collect(Collectors.toMap(Vendor::getCVenCode, vendor -> vendor));
        Map<String, ComputationUnit> unitMap = computationUnitRepository.findByCComunitCodeIn(unitCodeSet).stream()
                .collect(Collectors.toMap(ComputationUnit::getCComunitCode, unit -> unit));
        Map<String, Person> personMap = personRepository.findByPersonCodeIn(personCodeSet).stream()
                .collect(Collectors.toMap(Person::getPersonCode, person -> person));
        
        // 转换每个查询结果
        for (Object[] row : results) {
            if (row.length >= 18) {
                PurchaseScanDTO dto = buildPurchaseScanDTOFromObjectArray(row, vendorMap, unitMap, personMap, batchNumber);
                if (dto != null) {
                    dtos.add(dto);
                }
            }
        }
        
        long convertTime = System.currentTimeMillis() - startTime;
        log.info("DTO转换完成, 耗时: {}ms, 结果数量: {}", convertTime, dtos.size());
        
        return dtos;
    }
    
    /**
     * 从Object[]构建PurchaseScanDTO
     */
    private PurchaseScanDTO buildPurchaseScanDTOFromObjectArray(Object[] row, Map<String, Vendor> vendorMap, 
                                                            Map<String, ComputationUnit> unitMap, Map<String, Person> personMap, String batchNumber) {
        try {
            PurchaseScanDTO dto = new PurchaseScanDTO();
            
            // 设置基础信息（根据查询字段的顺序）
            dto.setcPOID(String.valueOf(row[2])); // cPOID
            
            // 转换Date到LocalDateTime
            Object dPODateObj = row[5];
            if (dPODateObj instanceof Date) {
                Date dPODate = (Date) dPODateObj;
                dto.setdPODate(dPODate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            } else if (dPODateObj instanceof java.sql.Date) {
                java.sql.Date dPODate = (java.sql.Date) dPODateObj;
                dto.setdPODate(dPODate.toLocalDate().atStartOfDay());
            }
            
            dto.setcVenCode(String.valueOf(row[3])); // cVenCode
            dto.setcDefine1(row[6] != null ? String.valueOf(row[6]) : null); // cDefine1
            dto.setcPersonCode(row[7] != null ? String.valueOf(row[7]) : null); // cPersonCode
            dto.setcDepCode(row[8] != null ? String.valueOf(row[8]) : null); // cDepCode
            
            // 转换Date到LocalDateTime
            Object dArriveDateObj = row[13];
            if (dArriveDateObj instanceof Date) {
                Date dArriveDate = (Date) dArriveDateObj;
                dto.setdArriveDate(dArriveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            } else if (dArriveDateObj instanceof java.sql.Date) {
                java.sql.Date dArriveDate = (java.sql.Date) dArriveDateObj;
                dto.setdArriveDate(dArriveDate.toLocalDate().atStartOfDay());
            }
            
            dto.setcInvCode(String.valueOf(row[10])); // cInvCode
            
            // 处理数值类型
            Object quantityObj = row[11];
            if (quantityObj instanceof BigDecimal) {
                dto.setiQuantity((BigDecimal) quantityObj);
            } else if (quantityObj instanceof BigInteger) {
                dto.setiQuantity(new BigDecimal((BigInteger) quantityObj));
            } else if (quantityObj instanceof Number) {
                dto.setiQuantity(BigDecimal.valueOf(((Number) quantityObj).doubleValue()));
            }
            
            Object rowNoObj = row[15];
            if (rowNoObj instanceof Integer) {
                dto.setIrowno((Integer) rowNoObj);
            } else if (rowNoObj instanceof BigDecimal) {
                dto.setIrowno(((BigDecimal) rowNoObj).intValue());
            } else if (rowNoObj instanceof Number) {
                dto.setIrowno(((Number) rowNoObj).intValue());
            }
            
            Object boxQuantityObj = row[14];
            if (boxQuantityObj instanceof BigDecimal) {
                dto.setBoxQuantity((BigDecimal) boxQuantityObj);
            } else if (boxQuantityObj instanceof BigInteger) {
                dto.setBoxQuantity(new BigDecimal((BigInteger) boxQuantityObj));
            } else if (boxQuantityObj instanceof Number) {
                dto.setBoxQuantity(BigDecimal.valueOf(((Number) boxQuantityObj).doubleValue()));
            }
            
            Object detailIdObj = row[9];
            if (detailIdObj instanceof Integer) {
                dto.setcSrcSubID((Integer) detailIdObj);
            } else if (detailIdObj instanceof BigDecimal) {
                dto.setcSrcSubID(((BigDecimal) detailIdObj).intValue());
            } else if (detailIdObj instanceof Number) {
                dto.setcSrcSubID(((Number) detailIdObj).intValue());
            }
            
            dto.setcItemName(row[16] != null ? String.valueOf(row[16]) : null); // cInvName
            
            
            // 设置供应商信息
            Object venCode = row[3];
            String venCodeStr = venCode != null ? String.valueOf(venCode) : "";
            Vendor vendor = vendorMap.get(venCodeStr);
            if (vendor != null) {
                dto.setSupplierName(vendor.getCVenName());
            }
            
            // 设置单位信息
            Object unitCode = row[17];
            String unitCodeStr = unitCode != null ? String.valueOf(unitCode) : "";
            ComputationUnit unit = unitMap.get(unitCodeStr);
            if (unit != null) {
                dto.setcUnitID(unit.getCComunitCode());
                dto.setUnitName(unit.getCComUnitName());
            } else {
                dto.setUnitName("未知单位");
            }
            
            // 设置人员信息
            Object personCode = row[7];
            String personCodeStr = personCode != null ? String.valueOf(personCode) : "";
            Person person = personMap.get(personCodeStr);
            if (person != null) {
                dto.setPersonName(person.getPersonName());
            } else {
                dto.setPersonName(null); // 没有对应人员则设为null
            }
            
            // 计算数量信息
            Object receivedQtyObj = row[12];
            BigDecimal receivedQty = BigDecimal.ZERO;
            if (receivedQtyObj instanceof BigDecimal) {
                receivedQty = (BigDecimal) receivedQtyObj;
            } else if (receivedQtyObj instanceof BigInteger) {
                receivedQty = new BigDecimal((BigInteger) receivedQtyObj);
            } else if (receivedQtyObj instanceof Number) {
                receivedQty = BigDecimal.valueOf(((Number) receivedQtyObj).doubleValue());
            }
            dto.setReceivedQuantity(receivedQty);
            
            BigDecimal totalQuantity = dto.getiQuantity();
            if (totalQuantity != null) {
                dto.setRemainingQuantity(totalQuantity.subtract(receivedQty));
            }
            
            // 获取该订单已经打码的最新的条码值
            String cPOID = dto.getcPOID();
            Integer detailId = dto.getcSrcSubID();
            if (cPOID != null && detailId != null) {
                try {
                    List<HYBarCodeMain> barCodeList = hyBarCodeMainRepository.findByCsrccodeAndCsrcsubidOrderByCreateTimeDesc(cPOID, detailId);
                    if (!barCodeList.isEmpty()) {
                        dto.setBarcode(barCodeList.get(0).getBarcode()); // 获取最新的条码值
                        dto.setBatchNumber(barCodeList.get(0).getPLot());
                    } else {
                        dto.setBarcode(null); // 没有条码则为null
                        dto.setBatchNumber(null);
                    }
                } catch (Exception e) {
                    log.warn("获取订单条码失败: cPOID={}, detailId={}, error={}", cPOID, detailId, e.getMessage());
                    dto.setBarcode(null);
                    dto.setBatchNumber(null);
                }
            } else {
                dto.setBarcode(null);
                dto.setBatchNumber(null);
            }
            
            return dto;
        } catch (Exception e) {
            log.error("构建DTO失败: {}", e.getMessage(), e);
            return null;
        }
    }
}