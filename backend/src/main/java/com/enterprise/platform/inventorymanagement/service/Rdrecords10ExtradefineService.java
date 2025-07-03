package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords10Extradefine;
import java.util.List;
import java.util.Optional;

public interface Rdrecords10ExtradefineService {
    List<Rdrecords10Extradefine> findAll();
    Optional<Rdrecords10Extradefine> findById(Long id);
    Rdrecords10Extradefine save(Rdrecords10Extradefine entity);
    void deleteById(Long id);
}