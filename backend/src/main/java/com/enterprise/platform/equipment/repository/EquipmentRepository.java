package com.enterprise.platform.equipment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enterprise.platform.equipment.enums.EquipmentStatus;
import com.enterprise.platform.equipment.model.Equipment;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, String> {
    List<Equipment> findByStatus(EquipmentStatus status);
    
    @Query("SELECT e FROM Equipment e WHERE " +
           "e.lastMaintenance IS NULL OR " +
           "DATEDIFF(CURRENT_DATE, e.lastMaintenance) > 30")
    List<Equipment> findEquipmentsNeedMaintenance();
}