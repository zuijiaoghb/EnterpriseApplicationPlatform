package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.CurrentStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrentStockRepository extends JpaRepository<CurrentStock, Integer> {
    @Query("SELECT c FROM CurrentStock c WHERE c.cInvCode = :cInvCode")
    List<CurrentStock> findByCInvCode(@Param("cInvCode") String cInvCode);

    @Query("SELECT MAX(c.itemId) FROM CurrentStock c")
    Integer findMaxItemId();

    @Query("SELECT c FROM CurrentStock c WHERE c.cWhCode = :cWhCode")
    List<CurrentStock> findByCWhCode(@Param("cWhCode") String cWhCode);

    @Query("SELECT c FROM CurrentStock c WHERE c.cInvCode = :cInvCode AND c.cWhCode = :cWhCode AND c.cBatch = :cBatch")
    Optional<CurrentStock> findByCInvCodeAndCWhCodeAndCBatch(@Param("cInvCode") String cInvCode, @Param("cWhCode") String cWhCode, @Param("cBatch") String cBatch);
    @Query("SELECT c FROM CurrentStock c WHERE c.cInvCode = :cInvCode AND c.cWhCode = :cWhCode")
    List<CurrentStock> findBycInvCodeAndCWhCode(@Param("cInvCode") String cInvCode, @Param("cWhCode") String cWhCode);

    @Modifying
    @Query("UPDATE CurrentStock c SET c.fInQuantity = c.fInQuantity + :quantity WHERE c.cInvCode = :cInvCode AND c.cWhCode = :cWhCode AND c.cBatch = :cBatch")
    int updateFInQuantityByCInvCodeAndCWhCodeAndCBatch(@Param("cInvCode") String cInvCode, @Param("cWhCode") String cWhCode, @Param("cBatch") String cBatch, @Param("quantity") BigDecimal quantity);
}