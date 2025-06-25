package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.MomOrderdetailDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomMorder;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrder;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.MomMorderRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.HYBarCodeMainRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.MomOrderRepository;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.MomOrderdetailRepository;
import com.enterprise.platform.inventorymanagement.service.MomOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class MomOrderServiceImpl implements MomOrderService {

    private final MomOrderRepository momOrderRepository;
    private final MomOrderdetailRepository momOrderdetailRepository;
    private final MomMorderRepository momMorderRepository;
    private final HYBarCodeMainRepository hyBarCodeMainRepository;

    @Autowired
    public MomOrderServiceImpl(MomOrderRepository momOrderRepository, 
                              MomOrderdetailRepository momOrderdetailRepository, 
                              MomMorderRepository momMorderRepository, 
                              HYBarCodeMainRepository hyBarCodeMainRepository) {
        this.momOrderRepository = momOrderRepository;
        this.momOrderdetailRepository = momOrderdetailRepository;
        this.momMorderRepository = momMorderRepository;
        this.hyBarCodeMainRepository = hyBarCodeMainRepository;
    }

    @Override
    public Optional<MomOrder> getOrderById(Integer moId) {
        return momOrderRepository.findById(moId);
    }

    @Override
    public MomOrder getOrderByCode(String moCode) {
        return momOrderRepository.findByMoCode(moCode)
                .orElseThrow(() -> new RuntimeException("生产订单不存在: " + moCode));
    }

    @Override
    public List<MomOrder> getAllOrders() {
        return momOrderRepository.findAll();
    }

    @Override
    public MomOrder createOrder(MomOrder momOrder) {
        // 设置默认值或业务规则验证
        if (momOrder.getUpdCount() == null) {
            momOrder.setUpdCount(0);
        }
        if (momOrder.getIPrintCount() == null) {
            momOrder.setIPrintCount(0);
        }
        return momOrderRepository.save(momOrder);
    }

    @Override
    public MomOrder updateOrder(Integer moId, MomOrder momOrderDetails) {
        return momOrderRepository.findById(moId)
                .map(existingOrder -> {
                    existingOrder.setMoCode(momOrderDetails.getMoCode());
                    existingOrder.setModifyDate(momOrderDetails.getModifyDate());
                    existingOrder.setModifyUser(momOrderDetails.getModifyUser());
                    existingOrder.setUpdCount(existingOrder.getUpdCount() + 1);
                    // 更新其他必要字段
                    return momOrderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + moId));
    }

    @Override
    public Optional<MomOrder> getOrderByBarcode(String barcode) {
        return hyBarCodeMainRepository.findByBarcode(barcode)
                .flatMap(hyBarCodeMain -> momOrderRepository.findByMoCode(hyBarCodeMain.getCsrccode()));
    }

    @Override
    public Optional<MomOrderdetailDTO> getOrderDetailByBarcode(String barcode) {
        return hyBarCodeMainRepository.findByBarcode(barcode)
                .filter(hyBarCodeMain -> hyBarCodeMain.getCsrcsubid() != null)
                .flatMap(hyBarCodeMain -> momOrderdetailRepository.findByMoDId(hyBarCodeMain.getCsrcsubid()))
                .map(orderDetail -> {
                    MomOrderdetailDTO dto = new MomOrderdetailDTO();
                    dto.setMoDId(orderDetail.getMoDId());
                    dto.setIQuantity(orderDetail.getQty());
                    dto.setInvCode(orderDetail.getInvCode());
                    dto.setMoLotCode(orderDetail.getMoLotCode());
                    return dto;
                });
    }

    @Override
    public Optional<MomMorder> getMomMorderByBarcode(String barcode) {
        return hyBarCodeMainRepository.findByBarcode(barcode)
                .filter(hyBarCodeMain -> hyBarCodeMain.getCsrcsubid() != null)
                .map(hyBarCodeMain -> momMorderRepository.findByMoDId(hyBarCodeMain.getCsrcsubid()))
                .orElse(Optional.empty());
    }

    @Override
    public void deleteOrder(Integer moId) {
        momOrderRepository.deleteById(moId);
    }
}