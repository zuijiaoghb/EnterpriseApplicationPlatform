package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords10;
import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Inventory;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrder;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrderdetail;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10Extradefine;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords10Extradefine;
import com.enterprise.platform.inventorymanagement.model.sqlserver.CurrentStock;
import com.enterprise.platform.inventorymanagement.model.dto.U8ToWmsDTO;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.HYBarCodeMainRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.InventoryRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.MomOrderRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.MomOrderdetailRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.Rdrecord10Repository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.Rdrecords10Repository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.Rdrecord10ExtradefineRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.Rdrecords10ExtradefineRepository;
import com.enterprise.platform.inventorymanagement.service.Rdrecord10Service;
import com.enterprise.platform.inventorymanagement.service.CurrentStockService;
import com.enterprise.platform.inventorymanagement.service.U8ToWmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "sqlServerTransactionManager")
public class Rdrecord10ServiceImpl implements Rdrecord10Service {

    /**
     * 查询当月以smscrk开头的最大流水号
     */
    private int getMaxInboundSerial(String yearMonth) {
        String prefix = "smscrk" + yearMonth;
        String maxCode = rdrecord10Repository.findMaxCCodeByPrefix(prefix + "%");
        if (maxCode == null) {
            return 0;
        }
        // 提取流水号部分(最后5位)
        String serialPart = maxCode.substring(prefix.length());
        return Integer.parseInt(serialPart);
    }

    private final Rdrecord10Repository rdrecord10Repository;
    private final Rdrecords10Repository rdrecords10Repository;
    private final HYBarCodeMainRepository hyBarcodeMainRepository;
    private final MomOrderRepository momOrderRepository;
    private final MomOrderdetailRepository momOrderdetailRepository;
    private final Rdrecord10ExtradefineRepository rdrecord10ExtradefineRepository;
    private final Rdrecords10ExtradefineRepository rdrecords10ExtradefineRepository;
    private final CurrentStockService currentStockService;
    private final U8ToWmsService u8ToWmsService;

    // 注入InventoryRepository
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public Optional<Rdrecord10> getInboundByBarcode(String barcode) {
        List<Rdrecords10> records = rdrecords10Repository.findByCBarCode(barcode);
        if (!records.isEmpty()) {
            Long mainId = records.get(0).getRdrecord10().getId();
            return rdrecord10Repository.findById(mainId);
        }
        return Optional.empty();
    }

    @Override
    public Rdrecord10 createInbound(Rdrecord10 inbound) {
        return rdrecord10Repository.save(inbound);
    }


    @Override
    public Rdrecord10 createInboundByBarcode(String barcode, String cwhcode, Integer iQuantity) {
        // 1. 查询条码信息
        HYBarCodeMain barcodeMain = hyBarcodeMainRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("条码不存在: " + barcode));

        // 2. 查询生产订单信息
        MomOrder momOrder = momOrderRepository.findByMoCode(barcodeMain.getCsrccode())
                .orElseThrow(() -> new RuntimeException("生产订单不存在: " + barcodeMain.getCsrccode()));

        MomOrderdetail orderDetail = momOrderdetailRepository.findByMoDId(barcodeMain.getCsrcsubid())
                .orElseThrow(() -> new RuntimeException("订单明细不存在: " + barcodeMain.getCsrcsubid()));

        // 3. 创建入库单主表记录
        Rdrecord10 inbound = new Rdrecord10();
        // 基础信息字段
        LocalDateTime now = LocalDateTime.now();
        // 生成年月部分 (格式: yyyyMM)
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        // 查询当月最大流水号并加1
        String serialNumber = String.format("%05d", getMaxInboundSerial(yearMonth) + 1);
        // 组合成完整入库单号
        inbound.setCCode("smscrk" + yearMonth + serialNumber);
        inbound.setBRdFlag((byte) 1);
        inbound.setCVouchType("10"); // 产成品入库单类型
        inbound.setCBusType("成品入库");
        inbound.setCSource("生产订单");
        inbound.setCBusCode(momOrder.getMoCode());
        inbound.setCWhCode(cwhcode); // 使用前端传入的仓库编码
        inbound.setDDate(LocalDate.now().atStartOfDay());
        inbound.setCRdCode("201");    
        inbound.setCDepCode(orderDetail.getMDeptCode());                   
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("当前用户未登录，无法创建产成品入库单");
        }
        inbound.setCMaker(authentication.getName());
        inbound.setBTransFlag(false);
        inbound.setBpufirst(false);
        inbound.setBiafirst(false);
        inbound.setVtId(63);
        inbound.setBIsSTQc(false);
        inbound.setIproorderid(momOrder.getMoId().longValue());
        inbound.setBFromPreYear(false);
        inbound.setBIsComplement((short)0);
        inbound.setIDiscountTaxType((byte)0);
        inbound.setIreturncount(0);
        inbound.setIverifystate(0);
        inbound.setIswfcontrolled(0);
        inbound.setDnmaketime(now);
        inbound.setBredvouch(0);
        inbound.setIPrintCount(0);
        inbound.setCsysbarcode("||st10|"+"smscrk" + yearMonth + serialNumber);               

        // 4. 保存主表
        Rdrecord10 savedInbound = rdrecord10Repository.save(inbound);

        // 5. 创建入库单明细记录
        Rdrecords10 inboundDetail = new Rdrecords10();
        inboundDetail.setRdrecord10(savedInbound);             
        inboundDetail.setCInvCode(orderDetail.getInvCode());
        // 验证传入的数量参数
        if (iQuantity == null || iQuantity <= 0) {
            throw new RuntimeException("入库数量必须大于0");
        }
        // 验证传入的数量参数要小于对应的生产订单mom_orderdetail的数量
        if (iQuantity > orderDetail.getQty().intValue()) {
            throw new RuntimeException("传入的入库数量不能大于生产订单明细中的数量");
        }
        inboundDetail.setIQuantity(new BigDecimal(iQuantity));
        inboundDetail.setINum(new BigDecimal(0));
        inboundDetail.setCBatch(barcodeMain.getPLot());
        inboundDetail.setCBarCode(barcode);
        inboundDetail.setIFlag((byte) 0);
        inboundDetail.setCDefine22(orderDetail.getDefine22());
        inboundDetail.setCDefine27(orderDetail.getDefine27());
        inboundDetail.setINQuantity(new BigDecimal(iQuantity));
        inboundDetail.setCDefine28(orderDetail.getDefine28());
        inboundDetail.setCDefine29(orderDetail.getDefine29());
        inboundDetail.setCDefine30(orderDetail.getDefine30());
        inboundDetail.setIMPoIds(orderDetail.getMoDId().longValue());
        inboundDetail.setBRelated(false);
        inboundDetail.setBLPUseFree(false);
        inboundDetail.setIRSRowNO(0);
        inboundDetail.setIOriTrackID(0L);
        inboundDetail.setBCosting(true);
        inboundDetail.setCmocode(momOrder.getMoCode());
        inboundDetail.setImoseq(orderDetail.getSortSeq());
        inboundDetail.setIExpiratDateCalcu((short)0);
        inboundDetail.setIorderdid(orderDetail.getOrderDId());
        inboundDetail.setIordertype(orderDetail.getOrderType());
        inboundDetail.setIordercode(orderDetail.getOrderCode());
        inboundDetail.setIorderseq(orderDetail.getOrderSeq());
        inboundDetail.setIsodid(orderDetail.getSoDId());
        inboundDetail.setIsotype(orderDetail.getSoType());
        inboundDetail.setCsocode(orderDetail.getSoCode());
        inboundDetail.setIsoseq(orderDetail.getSoSeq());
        inboundDetail.setCplanlotcode(orderDetail.getLPlanCode());
        
        // 6. 保存明细
        Rdrecords10 savedDetail = rdrecords10Repository.save(inboundDetail);

        // 7. 创建并保存主表扩展记录
        Rdrecord10Extradefine recordExtradefine = new Rdrecord10Extradefine();
        recordExtradefine.setID(savedInbound.getId());
        recordExtradefine.setChdefine2(null); // 根据实际业务需求设置值
        rdrecord10ExtradefineRepository.save(recordExtradefine);

        // 8. 创建并保存明细表扩展记录
        Rdrecords10Extradefine recordsExtradefine = new Rdrecords10Extradefine();
        recordsExtradefine.setAutoID(savedDetail.getAutoId());
        recordsExtradefine.setCbdefine1(null); // 根据实际业务需求设置值
        recordsExtradefine.setCbdefine3(null); // 根据实际业务需求设置值
        recordsExtradefine.setCbdefine4(null); // 根据实际业务需求设置值
        rdrecords10ExtradefineRepository.save(recordsExtradefine);

        // 仅更新生产订单明细的已入库数量字段，避免修改其他字段
        momOrderdetailRepository.updateQualifiedInQty(orderDetail.getMoDId(), new BigDecimal(iQuantity));

        // 创建并保存当前库存记录
        Optional<CurrentStock> existingStock = currentStockService.findByCInvCodeAndCWhCodeAndCBatch(orderDetail.getInvCode(), cwhcode, barcodeMain.getPLot());
        if (existingStock.isPresent()) {            
            currentStockService.updateFInQuantity(orderDetail.getInvCode(), cwhcode, barcodeMain.getPLot(), savedDetail.getIQuantity());
        } else {
            CurrentStock currentStock = new CurrentStock();
            currentStock.setCWhCode(cwhcode);
            currentStock.setCInvCode(orderDetail.getInvCode());
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
            currentStock.setFInQuantity(savedDetail.getIQuantity());
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
        u8ToWmsDTO.setID(savedInbound.getId());
        u8ToWmsDTO.setVoucherType("产成品入库"); 
        u8ToWmsDTO.setCCode(savedInbound.getCCode());
        u8ToWmsDTO.setDDate(savedInbound.getDDate().toLocalDate());
        u8ToWmsDTO.setCDepCode(orderDetail.getMDeptCode());
        u8ToWmsDTO.setCDepName("");
        u8ToWmsDTO.setCVenCode(null);
        u8ToWmsDTO.setCVenName(null);
        u8ToWmsDTO.setCWhCode(savedInbound.getCWhCode());  
        u8ToWmsDTO.setCWhName("");   
        u8ToWmsDTO.setCPersonCode(null);
        u8ToWmsDTO.setCPersonName(null);  
        u8ToWmsDTO.setCDeliverCode("");
        u8ToWmsDTO.setCMemo(savedInbound.getCMemo());
        u8ToWmsDTO.setCMaker(savedInbound.getCMaker());
        u8ToWmsDTO.setAutoID(savedDetail.getAutoId());
        u8ToWmsDTO.setIRowNo(savedDetail.getIrowno());        
        u8ToWmsDTO.setCInvCode(savedDetail.getCInvCode());
        // 查询并设置cInvName
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(u8ToWmsDTO.getCInvCode());
        if (inventoryOpt.isPresent()) {
            u8ToWmsDTO.setCInvName(inventoryOpt.get().getCInvName());
        } else {
            u8ToWmsDTO.setCInvName("");
        }
        u8ToWmsDTO.setCInvStd(inventoryOpt.get().getCInvStd());
        u8ToWmsDTO.setWb("");
        u8ToWmsDTO.setCBatch(savedDetail.getCBatch());
        u8ToWmsDTO.setCSnCode(null);
        u8ToWmsDTO.setCUnitCode(inventoryOpt.get().getCComUnitCode());
        u8ToWmsDTO.setCUnitName("");        
        u8ToWmsDTO.setIQuantity(savedDetail.getIQuantity());
        u8ToWmsDTO.setIOriTaxCost(null);
        u8ToWmsDTO.setIOriTaxMoney(null);
        u8ToWmsDTO.setCbMemo(savedDetail.getCbMemo());
        u8ToWmsDTO.setU8CreateDatetime(now);
        u8ToWmsDTO.setU8Status("新增");
        u8ToWmsDTO.setWmsReadError(null);
        u8ToWmsDTO.setCInvCCode(inventoryOpt.get().getCInvCCode());
        u8ToWmsService.save(u8ToWmsDTO);

        return savedInbound;        
    }


}