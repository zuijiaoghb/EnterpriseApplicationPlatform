package com.enterprise.platform.inventorymanagement.repository.sqlserver;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {
}