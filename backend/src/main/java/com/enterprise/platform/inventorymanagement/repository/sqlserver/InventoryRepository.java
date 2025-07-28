package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Inventory;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {
    @Query("SELECT i FROM Inventory i WHERE i.cInvCode IN :cInvCodes")
    List<Inventory> findByCInvCodeIn(@Param("cInvCodes") Set<String> cInvCodes);
}