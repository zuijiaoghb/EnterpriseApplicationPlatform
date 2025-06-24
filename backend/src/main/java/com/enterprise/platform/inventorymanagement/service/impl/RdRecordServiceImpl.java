package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.sqlserver.*;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.*;
import com.enterprise.platform.inventorymanagement.service.RdRecordService;
import lombok.RequiredArgsConstructor;

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

        // 5. 创建入库单子表
        RdRecords01 inboundDetail = new RdRecords01();
        inboundDetail.setId(inboundMain.getId());
        inboundDetail.setCInvCode(poPodetails.getcInvCode());
        inboundDetail.setIQuantity(new BigDecimal(quantity));
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
        rdRecords01Repository.save(inboundDetail);        

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