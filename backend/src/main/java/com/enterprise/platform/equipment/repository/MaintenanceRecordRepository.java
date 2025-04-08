package com.enterprise.platform.equipment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enterprise.platform.equipment.model.MaintenanceRecordWithBLOBs;

import java.util.List;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecordWithBLOBs, Long> {
    List<MaintenanceRecordWithBLOBs> findByEquipmentId(String equipmentId);
}