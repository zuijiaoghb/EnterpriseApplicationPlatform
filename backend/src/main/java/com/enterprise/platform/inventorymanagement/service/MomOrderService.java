package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.sqlserver.MomMorder;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrder;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrderdetail;

import java.util.List;
import java.util.Optional;

public interface MomOrderService {
    // 根据ID查询订单
    Optional<MomOrder> getOrderById(Integer moId);

    // 根据订单编号查询订单
    MomOrder getOrderByCode(String moCode);

    // 根据条码查询订单
    Optional<MomOrder> getOrderByBarcode(String barcode);

    // 根据条码查询订单明细
    Optional<MomOrderdetail> getOrderDetailByBarcode(String barcode);

    // 根据条码查询生产订单(mom_morder)
    Optional<MomMorder> getMomMorderByBarcode(String barcode);

    // 查询所有订单
    List<MomOrder> getAllOrders();

    // 创建订单
    MomOrder createOrder(MomOrder momOrder);

    // 更新订单
    MomOrder updateOrder(Integer moId, MomOrder momOrderDetails);

    // 删除订单
    void deleteOrder(Integer moId);
}