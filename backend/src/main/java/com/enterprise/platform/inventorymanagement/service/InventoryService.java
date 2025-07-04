package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.dto.InventoryDTO;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<InventoryDTO> getAllInventories();
    Optional<InventoryDTO> getInventoryByCode(String cInvCode);
    List<InventoryDTO> getInventoriesByName(String cInvName);
}