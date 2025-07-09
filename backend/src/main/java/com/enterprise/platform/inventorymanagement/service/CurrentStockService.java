package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.dto.CurrentStockDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.CurrentStock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CurrentStockService {
    List<CurrentStockDTO> findByCInvCodeDTO(String cInvCode);
    List<CurrentStock> findByCInvCode(String cInvCode);
    Integer findMaxItemId();
    List<CurrentStockDTO> findByCWhCode(String cWhCode);
    List<CurrentStockDTO> findByCInvCodeAndCWhCode(String cInvCode, String cWhCode);
    CurrentStock saveCurrentStock(CurrentStock currentStock);
    Optional<CurrentStock> findByCInvCodeAndCWhCodeAndCBatch(String cInvCode, String cWhCode, String cBatch);
    int updateFInQuantity(String cInvCode, String cWhCode, String cBatch, BigDecimal quantity);
}