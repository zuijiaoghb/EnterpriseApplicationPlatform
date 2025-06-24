package com.enterprise.platform.inventorymanagement.controller;

import com.enterprise.platform.inventorymanagement.model.sqlserver.MomMorder;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrder;
import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrderdetail;
import com.enterprise.platform.inventorymanagement.service.MomOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mom-orders")
public class MomOrderController {

    private final MomOrderService momOrderService;

    @Autowired
    public MomOrderController(MomOrderService momOrderService) {
        this.momOrderService = momOrderService;
    }

    // 获取订单详情 by ID
    @GetMapping("/{id}")
    public ResponseEntity<MomOrder> getOrderById(@PathVariable Integer id) {
        Optional<MomOrder> order = momOrderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 获取订单详情 by 订单编号
    @GetMapping("/code/{moCode}")
    public ResponseEntity<MomOrder> getOrderByCode(@PathVariable String moCode) {
        MomOrder order = momOrderService.getOrderByCode(moCode);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    // 根据条码查询订单
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<MomOrder> getOrderByBarcode(@PathVariable String barcode) {
        Optional<MomOrder> order = momOrderService.getOrderByBarcode(barcode);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 根据条码查询订单明细
    @GetMapping("/barcode/detail/{barcode}")
    public ResponseEntity<MomOrderdetail> getOrderDetailByBarcode(@PathVariable String barcode) {
        Optional<MomOrderdetail> orderDetail = momOrderService.getOrderDetailByBarcode(barcode);
        return orderDetail.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 根据条码查询生产订单(mom_morder)
    @GetMapping("/barcode/morder/{barcode}")
    public ResponseEntity<MomMorder> getMomMorderByBarcode(@PathVariable String barcode) {
        Optional<MomMorder> momMorder = momOrderService.getMomMorderByBarcode(barcode);
        return momMorder.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 获取所有订单
    @GetMapping
    public List<MomOrder> getAllOrders() {
        return momOrderService.getAllOrders();
    }

    // 创建新订单
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MomOrder createOrder(@RequestBody MomOrder momOrder) {
        return momOrderService.createOrder(momOrder);
    }

    // 更新订单
    @PutMapping("/{id}")
    public ResponseEntity<MomOrder> updateOrder(@PathVariable Integer id, @RequestBody MomOrder momOrderDetails) {
        try {
            MomOrder updatedOrder = momOrderService.updateOrder(id, momOrderDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除订单
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        momOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}