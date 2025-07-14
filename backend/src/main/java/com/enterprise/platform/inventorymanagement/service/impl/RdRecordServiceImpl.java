package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.U8ToWmsDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.*;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.*;
import com.enterprise.platform.inventorymanagement.service.RdRecordService;
import com.enterprise.platform.inventorymanagement.service.CurrentStockService;
import com.enterprise.platform.inventorymanagement.service.U8ToWmsService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "sqlServerTransactionManager")
public class RdRecordServiceImpl implements RdRecordService {
    /**
     * 查询当月以smcgrk开头的最大流水号
     */
    private int getMaxInboundSerial(String yearMonth) {
        String prefix = "smcgrk" + yearMonth;
        String maxCode = rdRecord01Repository.findMaxCCodeByPrefix(prefix + "%");
        if (maxCode == null) {
            return 0;
        }
        // 提取流水号部分(最后5位)
        String serialPart = maxCode.substring(prefix.length());
        return Integer.parseInt(serialPart);
    }

    private final RdRecord01Repository rdRecord01Repository;
    private final RdRecords01Repository rdRecords01Repository;
    private final HYBarCodeMainRepository hyBarCodeMainRepository;
    private final PO_PomainRepository poPomainRepository;
    private final PO_PodetailsRepository poPodetailsRepository;
    private final RdRecord01ExtradefineRepository rdRecord01ExtradefineRepository;
    private final Rdrecords01ExtradefineRepository rdrecords01ExtradefineRepository;
    private final CurrentStockService currentStockService;
    private final U8ToWmsService u8ToWmsService;

    // 注入InventoryRepository
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public RdRecord01 getInboundByPOCode(String poCode) {
        return rdRecord01Repository.findBycCode(poCode);
    }

    @Override
    public RdRecord01 createInboundByBarcode(String barcode, String warehouseCode, Integer quantity) {
        // 1. 查询条码信息
        Optional<HYBarCodeMain> barcodeOptional = hyBarCodeMainRepository.findByBarcode(barcode);
        if (barcodeOptional.isEmpty()) {
            throw new RuntimeException("条码不存在: " + barcode);
        }
        HYBarCodeMain barcodeMain = barcodeOptional.get();

        // 2. 查询采购订单信息
        String poCode = barcodeMain.getCsrccode();
        PO_Pomain poPomain = poPomainRepository.findBycPOID(poCode);
        if (poPomain == null) {
            throw new RuntimeException("采购订单不存在: " + poCode);
        }

        // 3. 查询采购订单子表信息
        Integer srcSubId = barcodeMain.getCsrcsubid();
        Optional<PO_Podetails> podetailsOptional = poPodetailsRepository.findById(srcSubId);
        if (podetailsOptional.isEmpty()) {
            throw new RuntimeException("采购订单子表记录不存在: " + srcSubId);
        }
        PO_Podetails poPodetails = podetailsOptional.get();

        // 4. 创建入库单主表
        RdRecord01 inboundMain = new RdRecord01();
        // 基础信息字段
        LocalDateTime now = LocalDateTime.now();
        inboundMain.setDDate(LocalDate.now().atStartOfDay());
        // 生成年月部分 (格式: yyyyMM)
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        // 查询当月最大流水号并加1
        String serialNumber = String.format("%05d", getMaxInboundSerial(yearMonth) + 1);
        
        // 组合成完整入库单号
        inboundMain.setCCode("smcgrk" + yearMonth + serialNumber);
        inboundMain.setCWhCode(warehouseCode);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("当前用户未登录，无法创建产成品入库单");
        }
        inboundMain.setCMaker(authentication.getName()); // 根据实际认证系统
        inboundMain.setCSource("采购订单");
        inboundMain.setBRdFlag((byte) 1);
        inboundMain.setCVouchType("01");
        // 补充用户要求的字段
        inboundMain.setCBusType("普通采购"); // 业务类型：采购入库
        inboundMain.setCRdCode("202"); // 入库单号前缀
        inboundMain.setCDepCode(poPomain.getcDepCode()); // 部门编码取自采购订单
        inboundMain.setCPersonCode(poPomain.getcPersonCode()); // 业务员
        inboundMain.setCPTCode("10"); // 采购类型
        inboundMain.setCVenCode(poPomain.getcVenCode()); // 供应商编码取自采购订单
        inboundMain.setCOrderCode(poCode); // 订单号
        inboundMain.setCDefine1("江西江特电机有限公司"); // 执行公司
        inboundMain.setCDefine10(""); // 送货单号
        inboundMain.setVtId(131430); // 单据类型ID
        inboundMain.setBIsSTQc(false); // 库存期初标志
        inboundMain.setBTransFlag(false); // 是否传递
        inboundMain.setIpurorderid(poPomain.getPoid().longValue()); // 采购订单ID（转换为Long类型）
        inboundMain.setITaxRate(poPomain.getiTaxRate().doubleValue()); // 税率取自采购主表
        inboundMain.setIExchRate(1.00); // 汇率默认1
        inboundMain.setCExchName("人民币"); // 币种
        inboundMain.setDnmaketime(LocalDateTime.now()); // 制单时间
        inboundMain.setBpufirst(false); // 采购期初标志
        inboundMain.setBiafirst(false); // 存货期初标志
        inboundMain.setBredvouch(0); // 红蓝标识
        inboundMain = rdRecord01Repository.save(inboundMain);

        // 创建主表扩展记录
        RdRecord01Extradefine extMain = new RdRecord01Extradefine();
        extMain.setID(inboundMain.getId());
        extMain.setChdefine1(null); 
        extMain.setChdefine2(null); 
        rdRecord01ExtradefineRepository.save(extMain);

        // 5. 创建入库单子表
        RdRecords01 inboundDetail = new RdRecords01();
        inboundDetail.setId(inboundMain.getId());
        inboundDetail.setCInvCode(poPodetails.getcInvCode());
        // 计算未入库量 = 采购订单数量 - 累计到货数量
        BigDecimal orderQuantity = poPodetails.getiQuantity();
        BigDecimal receivedQuantity = poPodetails.getiReceivedQTY() != null ? poPodetails.getiReceivedQTY() : BigDecimal.ZERO;
        BigDecimal remainingQuantity = orderQuantity.subtract(receivedQuantity);
        
        // 校验入库数量是否超过未入库量
        BigDecimal inboundQuantity = new BigDecimal(quantity);
        if (inboundQuantity.compareTo(remainingQuantity) > 0) {
            throw new RuntimeException("采购入库单数量不能超过未入库量，未入库量为: " + remainingQuantity);
        }
        
        inboundDetail.setIQuantity(inboundQuantity);
        inboundDetail.setINum(poPodetails.getiNum());
        inboundDetail.setCBatch(barcodeMain.getPLot());
        Date expirationDate = barcodeMain.getDExpirationdate();
        if (expirationDate != null) {
            inboundDetail.setDExpirationdate(expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        // 补充用户要求的字段
        inboundDetail.setIUnitCost(poPodetails.getiUnitPrice()); // 单价取自采购子表
        inboundDetail.setIPrice(poPodetails.getiMoney()); // 原币无税金额 
        inboundDetail.setIAPrice(poPodetails.getiNatMoney()); // 本币无税金额 
        inboundDetail.setIPOsID(poPodetails.getId().longValue()); // 采购子表ID
        inboundDetail.setFACost(poPodetails.getiNatUnitPrice()); // 本币无税单价 
        inboundDetail.setINQuantity(BigDecimal.valueOf(quantity)); // 数量
        inboundDetail.setCDefine33(poPodetails.getcDefine33()); // 自定义项33请购单号
        inboundDetail.setChVencode(poPomain.getcVenCode()); // 供应商编码
        inboundDetail.setIOriTaxCost(poPodetails.getiTaxPrice()); // 含税单价
        inboundDetail.setIOriCost(poPodetails.getiNatUnitPrice()); // 本币无税单价
        inboundDetail.setIOriMoney(poPodetails.getiNatMoney()); // 无税金额
        inboundDetail.setIOriTaxPrice(poPodetails.getiNatTax()); // 本币税额 
        inboundDetail.setIoriSum(poPodetails.getiNatSum()); // 本币价税合计
        inboundDetail.setITaxRate(poPodetails.getiPerTaxRate()); // 税率
        inboundDetail.setITaxPrice(poPodetails.getiTax()); // 税额
        inboundDetail.setISum(poPodetails.getiSum()); // 价税合计
        inboundDetail.setBTaxCost(poPodetails.getbTaxCost()); // 税额计算方式
        inboundDetail.setCPOID(poCode); // 采购订单号
        inboundDetail.setIrowno(1); // 行号
        inboundDetail.setIFlag((byte) 0);
        inboundDetail.setIMatSettleState(0);
        inboundDetail.setIBillSettleCount(0);
        inboundDetail.setBCosting(true);    // 单据是否核算
        inboundDetail.setISQuantity(new BigDecimal(0)); // 累计结算数量
        inboundDetail.setISNum(new BigDecimal(0));  //累计结算辅计量数量 
        inboundDetail.setIMoney(new BigDecimal(0)); 
        inboundDetail.setCDefine30(poPodetails.getcDefine30());
        inboundDetail.setCDefine31(poPodetails.getcDefine31());
        inboundDetail.setCDefine32(poPodetails.getcDefine32());
        inboundDetail.setBLPUseFree(false);
        inboundDetail.setIOriTrackID(0L);
        inboundDetail.setIExpiratDateCalcu((short) 0);
        inboundDetail.setIordertype(poPodetails.getIordertype());
        inboundDetail.setIorderdid(poPodetails.getIorderdid());
        inboundDetail.setIordercode(poPodetails.getCsoordercode());
        inboundDetail.setIorderseq(poPodetails.getIorderseq());
        inboundDetail.setIsodid(poPodetails.getSoDId());
        inboundDetail.setIsotype(poPodetails.getSoType());
        inboundDetail.setIsoseq(poPodetails.getIorderseq());
        inboundDetail.setCplanlotcode(poPodetails.getPlanlotnumber());
        inboundDetail.setBgift(poPodetails.getBgift());
        inboundDetail = rdRecords01Repository.save(inboundDetail);        

        // 创建子表扩展记录
        Rdrecords01Extradefine extDetail = new Rdrecords01Extradefine();
        extDetail.setAutoID(inboundDetail.getAutoId());
        extDetail.setCbdefine1(null); 
        extDetail.setCbdefine3(null); 
        extDetail.setCbdefine4(null); 
        rdrecords01ExtradefineRepository.save(extDetail);

        // 更新对应的采购订单子表PO_Podetails的累计到货数量iReceivedQTY和累计原币到货金额iReceivedMoney
        BigDecimal iQuantity = Optional.ofNullable(poPodetails.getiQuantity()).orElse(BigDecimal.ZERO);
        BigDecimal iMoney = Optional.ofNullable(poPodetails.getiMoney()).orElse(BigDecimal.ZERO);
        BigDecimal receivedQTY = Optional.ofNullable(poPodetails.getiReceivedQTY()).orElse(BigDecimal.ZERO);
        BigDecimal newReceivedQTY = receivedQTY.add(new BigDecimal(quantity));
        if (newReceivedQTY.compareTo(iQuantity) > 0) {
            throw new IllegalArgumentException("累计到货数量不能超过订单数量: " + iQuantity);
        }
        poPodetails.setiReceivedQTY(newReceivedQTY);
        
        BigDecimal receivedMoney = Optional.ofNullable(poPodetails.getiReceivedMoney()).orElse(BigDecimal.ZERO);
        BigDecimal unitPrice = Optional.ofNullable(poPodetails.getiUnitPrice()).orElse(BigDecimal.ZERO);
        BigDecimal addMoney = unitPrice.multiply(new BigDecimal(quantity));
        BigDecimal newReceivedMoney = receivedMoney.add(addMoney);
        if (newReceivedMoney.compareTo(iMoney) > 0) {
            throw new IllegalArgumentException("累计到货金额不能超过订单金额: " + iMoney);
        }
        poPodetails.setiReceivedMoney(newReceivedMoney);
        poPodetailsRepository.save(poPodetails);

        // 创建并保存当前库存记录
        Optional<CurrentStock> existingStock = currentStockService.findByCInvCodeAndCWhCodeAndCBatch(poPodetails.getcInvCode(), warehouseCode, barcodeMain.getPLot());
        if (existingStock.isPresent()) {            
            currentStockService.updateFInQuantity(poPodetails.getcInvCode(), warehouseCode, barcodeMain.getPLot(), inboundDetail.getIQuantity());
        } else {
            CurrentStock currentStock = new CurrentStock();
            currentStock.setCWhCode(warehouseCode);
            currentStock.setCInvCode(poPodetails.getcInvCode());
            List<CurrentStock> existingStocks = currentStockService.findByCInvCode(currentStock.getCInvCode());
            if (existingStocks != null && !existingStocks.isEmpty()) {
                currentStock.setItemId(existingStocks.get(0).getItemId());
            } else {
                Integer maxItemId = currentStockService.findMaxItemId();
                currentStock.setItemId(maxItemId != null ? maxItemId + 1 : 1);
            }
            currentStock.setCBatch(barcodeMain.getPLot());
            currentStock.setISoType(0);
            currentStock.setISodid("");
            currentStock.setIQuantity(BigDecimal.ZERO);
            currentStock.setINum(BigDecimal.ZERO);
            currentStock.setFInQuantity(inboundDetail.getIQuantity());
            currentStock.setFInNum(BigDecimal.ZERO);
            currentStock.setFOutQuantity(BigDecimal.ZERO);
            currentStock.setFOutNum(BigDecimal.ZERO);
            currentStock.setBStopFlag(false);
            currentStock.setFTransInQuantity(BigDecimal.ZERO);
            currentStock.setFTransInNum(BigDecimal.ZERO);
            currentStock.setFTransOutQuantity(BigDecimal.ZERO);
            currentStock.setFTransOutNum(BigDecimal.ZERO);
            currentStock.setFPlanQuantity(BigDecimal.ZERO);
            currentStock.setFPlanNum(BigDecimal.ZERO);
            currentStock.setFDisableQuantity(BigDecimal.ZERO);
            currentStock.setFDisableNum(BigDecimal.ZERO);
            currentStock.setFAvaQuantity(BigDecimal.ZERO);
            currentStock.setFAvaNum(BigDecimal.ZERO);
            currentStock.setBGSPSTOP(false);
            currentStock.setCMassUnit((short)0);
            currentStock.setFStopQuantity(BigDecimal.ZERO);
            currentStock.setFStopNum(BigDecimal.ZERO);
            currentStock.setIExpiratDateCalcu((short)0);
            currentStock.setIpeqty(BigDecimal.ZERO);
            currentStock.setIpenum(BigDecimal.ZERO);
            currentStockService.saveCurrentStock(currentStock);
        }

        // 同步创建U8ToWms记录
        U8ToWmsDTO u8ToWmsDTO = new U8ToWmsDTO();
        u8ToWmsDTO.setID(inboundMain.getId());
        u8ToWmsDTO.setVoucherType("采购入库"); // 采购入库单类型
        u8ToWmsDTO.setCCode(inboundMain.getCCode());
        u8ToWmsDTO.setDDate(inboundMain.getDDate().toLocalDate());
        u8ToWmsDTO.setCDepCode(poPomain.getcDepCode());
        u8ToWmsDTO.setCDepName("");
        u8ToWmsDTO.setCVenCode(inboundMain.getCVenCode());
        u8ToWmsDTO.setCVenName("");
        u8ToWmsDTO.setCWhCode(inboundMain.getCWhCode());  
        u8ToWmsDTO.setCWhName("");   
        u8ToWmsDTO.setCPersonCode(poPomain.getcPersonCode());
        u8ToWmsDTO.setCPersonName("");  
        u8ToWmsDTO.setCDeliverCode(null);
        u8ToWmsDTO.setCMemo(inboundMain.getCMemo());
        u8ToWmsDTO.setCMaker(inboundMain.getCMaker());
        u8ToWmsDTO.setAutoID(inboundDetail.getAutoId());
        u8ToWmsDTO.setIRowNo(inboundDetail.getIrowno());        
        u8ToWmsDTO.setCInvCode(inboundDetail.getCInvCode());
        // 查询并设置cInvName
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(u8ToWmsDTO.getCInvCode());
        if (inventoryOpt.isPresent()) {
            u8ToWmsDTO.setCInvName(inventoryOpt.get().getCInvName());
        } else {
            u8ToWmsDTO.setCInvName("");
        }
        u8ToWmsDTO.setCInvStd(inventoryOpt.get().getCInvStd());
        u8ToWmsDTO.setWb(null);
        u8ToWmsDTO.setCBatch(inboundDetail.getCBatch());
        u8ToWmsDTO.setCSnCode(null);
        u8ToWmsDTO.setCUnitCode(inventoryOpt.get().getCComUnitCode());
        u8ToWmsDTO.setCUnitName("");        
        u8ToWmsDTO.setIQuantity(inboundDetail.getIQuantity());
        u8ToWmsDTO.setIOriTaxCost(inboundDetail.getIOriTaxCost());
        u8ToWmsDTO.setIOriTaxMoney(inboundDetail.getIQuantity().multiply(inboundDetail.getIOriTaxCost()));
        u8ToWmsDTO.setCbMemo(inboundDetail.getCbMemo());
        u8ToWmsDTO.setU8CreateDatetime(now);
        u8ToWmsDTO.setU8Status("新增");
        u8ToWmsDTO.setWmsReadError(null);
        u8ToWmsDTO.setCInvCCode(inventoryOpt.get().getCInvCCode());
        u8ToWmsService.save(u8ToWmsDTO);

        return inboundMain;
    }

    @Override
    public List<RdRecords01> getInboundDetails(Long inboundId) {
        return rdRecords01Repository.findAllById(inboundId);
    }

    @Override    
    public RdRecord01 confirmInbound(Long inboundId) {
        Optional<RdRecord01> inboundOptional = rdRecord01Repository.findById(inboundId);
        if (inboundOptional.isEmpty()) {
            throw new RuntimeException("入库单不存在: " + inboundId);
        }

        RdRecord01 inboundMain = inboundOptional.get();
        //inboundMain.setDVeriDate(LocalDateTime.now());
        //inboundMain.setCAccounter("admin"); // 临时使用默认用户，需根据实际认证系统调整
        //inboundMain.setIverifystate(1); // 已审核状态
        return rdRecord01Repository.save(inboundMain);        
    }
}
