package com.enterprise.platform.inventorymanagement.service;

import com.enterprise.platform.inventorymanagement.model.sqlserver.Rdrecords01Extradefine;
import java.util.List;
import java.util.Optional;

public interface Rdrecords01ExtradefineService {
    List<Rdrecords01Extradefine> findAll();
    Optional<Rdrecords01Extradefine> findById(Long id);
    Rdrecords01Extradefine save(Rdrecords01Extradefine record);
    Rdrecords01Extradefine update(Long id, Rdrecords01Extradefine record);
    void deleteById(Long id);
}