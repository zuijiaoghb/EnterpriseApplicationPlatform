package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrderdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface MomOrderdetailRepository extends JpaRepository<MomOrderdetail, Integer> {
    // 根据订单ID查询子表记录
    List<MomOrderdetail> findByMomOrder_MoId(Integer moId);

    // 根据MoDId查询订单明细
    Optional<MomOrderdetail> findByMoDId(Integer moDId);
    
    // 仅更新已入库数量字段，避免修改其他字段
    @Query(value = "UPDATE mom_orderdetail SET QualifiedInQty = QualifiedInQty + :quantity WHERE MoDId = :id", nativeQuery = true)
    @Modifying
    @Transactional(transactionManager = "sqlServerTransactionManager")
    int updateQualifiedInQty(@Param("id") Integer id, @Param("quantity") BigDecimal quantity);
}