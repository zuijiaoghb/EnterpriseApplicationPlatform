package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.dto.U8ToWmsDTO;
import java.util.List;
import java.util.Optional;

public interface U8ToWmsService {
    Optional<U8ToWmsDTO> getById(Long id);
    List<U8ToWmsDTO> getAll();
    U8ToWmsDTO save(U8ToWmsDTO u8ToWmsDTO);
    U8ToWmsDTO update(Long id, U8ToWmsDTO u8ToWmsDTO);
    void delete(Long id);
}