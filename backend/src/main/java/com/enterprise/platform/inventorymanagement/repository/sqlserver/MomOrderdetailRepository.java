package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.MomOrderdetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MomOrderdetailRepository extends JpaRepository<MomOrderdetail, Integer> {
    // 根据订单ID查询子表记录
    List<MomOrderdetail> findByMomOrder_MoId(Integer moId);

    // 根据MoDId查询订单明细
    Optional<MomOrderdetail> findByMoDId(Integer moDId);
}