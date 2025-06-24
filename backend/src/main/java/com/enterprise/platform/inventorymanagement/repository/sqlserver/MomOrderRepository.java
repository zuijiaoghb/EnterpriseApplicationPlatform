package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MomOrderRepository extends JpaRepository<MomOrder, Integer> {
    // 根据订单编号查询订单
    MomOrder findByMoCode(String moCode);
}