package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.MomMorder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MomMorderRepository extends JpaRepository<MomMorder, Integer> {
    // 根据订单ID查询生产订单记录
    List<MomMorder> findByMomOrder_MoId(Integer moId);

    // 根据MoDId查询生产订单
    Optional<MomMorder> findByMoDId(Integer moDId);
}