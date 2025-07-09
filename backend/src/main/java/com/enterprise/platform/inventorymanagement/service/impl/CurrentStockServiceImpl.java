package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.CurrentStockDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.CurrentStock;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.CurrentStockRepository;
import com.enterprise.platform.inventorymanagement.service.CurrentStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "sqlServerTransactionManager")
public class CurrentStockServiceImpl implements CurrentStockService {

    private final CurrentStockRepository currentStockRepository;

    @Autowired
    public CurrentStockServiceImpl(CurrentStockRepository currentStockRepository) {
        this.currentStockRepository = currentStockRepository;
    }

    @Override
    public List<CurrentStockDTO> findByCInvCodeDTO(String cInvCode) {
        return currentStockRepository.findByCInvCode(cInvCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CurrentStockDTO> findByCWhCode(String cWhCode) {
        return currentStockRepository.findByCWhCode(cWhCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CurrentStockDTO> findByCInvCodeAndCWhCode(String cInvCode, String cWhCode) {
        return currentStockRepository.findBycInvCodeAndCWhCode(cInvCode, cWhCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CurrentStock saveCurrentStock(CurrentStock currentStock) {
        return currentStockRepository.save(currentStock);
    }

    @Override
    public Optional<CurrentStock> findByCInvCodeAndCWhCodeAndCBatch(String cInvCode, String cWhCode, String cBatch) {
        return currentStockRepository.findByCInvCodeAndCWhCodeAndCBatch(cInvCode, cWhCode, cBatch);
    }

    @Override
    public int updateFInQuantity(String cInvCode, String cWhCode, String cBatch, BigDecimal quantity) {
        return currentStockRepository.updateFInQuantityByCInvCodeAndCWhCodeAndCBatch(cInvCode, cWhCode, cBatch, quantity);
    }

    @Override
    public List<CurrentStock> findByCInvCode(String cInvCode) {
        return currentStockRepository.findByCInvCode(cInvCode);
    }

    @Override
    public Integer findMaxItemId() {
        return currentStockRepository.findMaxItemId();
    }

    private CurrentStockDTO convertToDTO(CurrentStock currentStock) {
        CurrentStockDTO dto = new CurrentStockDTO();
        dto.setAutoId(currentStock.getAutoId());
        dto.setCWhCode(currentStock.getCWhCode());
        dto.setCInvCode(currentStock.getCInvCode());
        dto.setItemId(currentStock.getItemId());
        dto.setCBatch(currentStock.getCBatch());
        dto.setIQuantity(currentStock.getIQuantity());
        dto.setINum(currentStock.getINum());
        dto.setCFree1(currentStock.getCFree1());
        dto.setCFree2(currentStock.getCFree2());
        dto.setFAvaQuantity(currentStock.getFAvaQuantity());
        dto.setFAvaNum(currentStock.getFAvaNum());
        dto.setFInQuantity(currentStock.getFInQuantity());
        dto.setDVDate(currentStock.getDVDate());
        dto.setCCheckState(currentStock.getCCheckState());
        dto.setDExpirationdate(currentStock.getDExpirationdate());
        dto.setIpeqty(currentStock.getIpeqty());
        dto.setIpenum(currentStock.getIpenum());
        return dto;
    }
}