package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecord01;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface RdRecord01Repository extends JpaRepository<RdRecord01, Long> {
    // 根据采购订单号查询入库单
    RdRecord01 findBycCode(String cCode);
    
    @Query("SELECT MAX(r.cCode) FROM RdRecord01 r WHERE r.cCode LIKE ?1")
    String findMaxCCodeByPrefix(String prefix);
}