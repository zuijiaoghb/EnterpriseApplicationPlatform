package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecords01;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RdRecords01Repository extends JpaRepository<RdRecords01, Long> {
    // 根据主表ID查询子表记录
    List<RdRecords01> findAllById(Long id);

    // 根据采购订单子表ID查询入库单子表记录
    List<RdRecords01> findByiPOsID(Long iPOsID);
}