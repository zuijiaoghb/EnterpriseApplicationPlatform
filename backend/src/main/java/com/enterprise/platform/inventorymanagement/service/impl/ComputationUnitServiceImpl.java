package com.enterprise.platform.inventorymanagement.service.impl;

import com.enterprise.platform.inventorymanagement.model.dto.ComputationUnitDTO;
import com.enterprise.platform.inventorymanagement.model.sqlserver.ComputationUnit;
import com.enterprise.platform.inventorymanagement.repository.sqlserver.ComputationUnitRepository;
import com.enterprise.platform.inventorymanagement.service.ComputationUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "sqlServerTransactionManager")
public class ComputationUnitServiceImpl implements ComputationUnitService {

    private final ComputationUnitRepository computationUnitRepository;

    @Override
    public List<ComputationUnitDTO> findAll() {
        return computationUnitRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ComputationUnitDTO findById(String cComunitCode) {
        ComputationUnit computationUnit = computationUnitRepository.findById(cComunitCode)
                .orElseThrow(() -> new RuntimeException("ComputationUnit not found with code: " + cComunitCode));
        return convertToDto(computationUnit);
    }

    @Override
    public ComputationUnitDTO save(ComputationUnitDTO computationUnitDTO) {
        ComputationUnit computationUnit = convertToEntity(computationUnitDTO);
        ComputationUnit savedComputationUnit = computationUnitRepository.save(computationUnit);
        return convertToDto(savedComputationUnit);
    }

    @Override
    public ComputationUnitDTO update(String cComunitCode, ComputationUnitDTO computationUnitDTO) {
        ComputationUnit existingComputationUnit = computationUnitRepository.findById(cComunitCode)
                .orElseThrow(() -> new RuntimeException("ComputationUnit not found with code: " + cComunitCode));
        BeanUtils.copyProperties(computationUnitDTO, existingComputationUnit, "cComunitCode");
        ComputationUnit updatedComputationUnit = computationUnitRepository.save(existingComputationUnit);
        return convertToDto(updatedComputationUnit);
    }

    @Override
    public void deleteById(String cComunitCode) {
        if (!computationUnitRepository.existsById(cComunitCode)) {
            throw new RuntimeException("ComputationUnit not found with code: " + cComunitCode);
        }
        computationUnitRepository.deleteById(cComunitCode);
    }

    private ComputationUnitDTO convertToDto(ComputationUnit computationUnit) {
        ComputationUnitDTO computationUnitDTO = new ComputationUnitDTO();
        BeanUtils.copyProperties(computationUnit, computationUnitDTO);
        return computationUnitDTO;
    }

    private ComputationUnit convertToEntity(ComputationUnitDTO computationUnitDTO) {
        ComputationUnit computationUnit = new ComputationUnit();
        BeanUtils.copyProperties(computationUnitDTO, computationUnit);
        return computationUnit;
    }
}