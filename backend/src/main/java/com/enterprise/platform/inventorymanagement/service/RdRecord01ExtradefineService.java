package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.sqlserver.RdRecord01Extradefine;
import java.util.List;
import java.util.Optional;

public interface RdRecord01ExtradefineService {
    List<RdRecord01Extradefine> findAll();
    Optional<RdRecord01Extradefine> findById(Long id);
    RdRecord01Extradefine save(RdRecord01Extradefine record);
    RdRecord01Extradefine update(Long id, RdRecord01Extradefine record);
    void deleteById(Long id);
}