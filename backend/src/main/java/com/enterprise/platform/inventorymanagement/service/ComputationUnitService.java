package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.dto.ComputationUnitDTO;
import java.util.List;

public interface ComputationUnitService {
    List<ComputationUnitDTO> findAll();
    ComputationUnitDTO findById(String cComunitCode);
    ComputationUnitDTO save(ComputationUnitDTO computationUnitDTO);
    ComputationUnitDTO update(String cComunitCode, ComputationUnitDTO computationUnitDTO);
    void deleteById(String cComunitCode);
}