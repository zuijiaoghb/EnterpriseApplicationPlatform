package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.InventoryDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.Inventory;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.InventoryRepository;
import com.enterprise.platform.inventorymanagement.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<InventoryDTO> getAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InventoryDTO> getInventoryByCode(String cInvCode) {
        return inventoryRepository.findById(cInvCode)
                .map(this::convertToDTO);
    }

    @Override
    public List<InventoryDTO> getInventoriesByName(String cInvName) {
        // 注意：这里需要根据实际JPA查询方法调整，当前假设使用name包含查询
        return inventoryRepository.findAll().stream()
                .filter(inventory -> inventory.getCInvName() != null && inventory.getCInvName().contains(cInvName))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setCInvCode(inventory.getCInvCode());
        dto.setCInvAddCode(inventory.getCInvAddCode());
        dto.setCInvName(inventory.getCInvName());
        dto.setCInvStd(inventory.getCInvStd());
        dto.setCInvCCode(inventory.getCInvCCode());
        dto.setCVenCode(inventory.getCVenCode());
        dto.setCPosition(inventory.getCPosition());
        dto.setBSale(inventory.getBSale());
        dto.setBPurchase(inventory.getBPurchase());
        dto.setITaxRate(inventory.getITaxRate());
        dto.setIInvSPrice(inventory.getIInvSPrice());
        dto.setIInvSCost(inventory.getIInvSCost());
        dto.setCInvABC(inventory.getCInvABC());
        dto.setDSDate(inventory.getDSDate());
        dto.setDEDate(inventory.getDEDate());
        dto.setCInvDefine1(inventory.getCInvDefine1());
        dto.setCInvDefine2(inventory.getCInvDefine2());
        dto.setCBarCode(inventory.getCBarCode());
        dto.setCEnglishName(inventory.getCEnglishName());
        dto.setCProduceNation(inventory.getCProduceNation());
        dto.setFRetailPrice(inventory.getFRetailPrice());
        return dto;
    }
}