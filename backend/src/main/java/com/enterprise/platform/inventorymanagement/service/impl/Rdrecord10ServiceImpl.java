package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords10;
import com.enterprise.platform.inventorymanagement.model.sqlserver.HYBarCodeMain;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrder;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrderdetail;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomMorder;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.HYBarCodeMainRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.MomOrderRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.MomOrderdetailRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.MomMorderRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.Rdrecord10Repository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.Rdrecords10Repository;
import com.enterprise.platform.inventorymanagement.service.Rdrecord10Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    private final MomMorderRepository momMorderRepository;
    

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

        MomMorder morder = momMorderRepository.findByMoDId(barcodeMain.getCsrcsubid())
                .orElseThrow(() -> new RuntimeException("生产订单明细不存在: " + barcodeMain.getCsrcsubid()));

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
        inbound.setCVouchType("10"); // 产成品入库单类型
        inbound.setCSource("生产订单");
        inbound.setCBusCode(momOrder.getMoCode());
        inbound.setCWhCode(cwhcode); // 使用前端传入的仓库编码
        inbound.setDDate(LocalDate.now().atStartOfDay());       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("当前用户未登录，无法创建产成品入库单");
        }
        inbound.setCMaker(authentication.getName());
        inbound.setBTransFlag(false);
        inbound.setBIsSTQc(false);
        // 设置其他必要字段...

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
        inboundDetail.setIQuantity(new BigDecimal(iQuantity));
        inboundDetail.setINum(new BigDecimal(0));
        inboundDetail.setCBatch(barcodeMain.getPLot());
        inboundDetail.setCBarCode(barcode);
        inboundDetail.setIFlag((byte) 0);
        // 设置其他必要字段...

        // 6. 保存明细
        rdrecords10Repository.save(inboundDetail);

        return savedInbound;
    }
    
    
}