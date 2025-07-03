package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecord10Extradefine;
import java.util.List;
import java.util.Optional;

public interface Rdrecord10ExtradefineService {
    List<Rdrecord10Extradefine> findAll();
    Optional<Rdrecord10Extradefine> findById(Long id);
    Rdrecord10Extradefine save(Rdrecord10Extradefine entity);
    void deleteById(Long id);
}