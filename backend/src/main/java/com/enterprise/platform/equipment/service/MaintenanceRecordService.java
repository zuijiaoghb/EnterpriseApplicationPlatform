package com.enterprise.platform.equipment.service;

import java.util.List;

import com.enterprise.platform.equipment.model.MaintenanceRecordWithBLOBs;

public interface MaintenanceRecordService {
    MaintenanceRecordWithBLOBs createRecord(MaintenanceRecordWithBLOBs record);
    List<MaintenanceRecordWithBLOBs> getAllRecords();
    MaintenanceRecordWithBLOBs getRecordById(Long id);
    MaintenanceRecordWithBLOBs updateRecord(Long id, MaintenanceRecordWithBLOBs recordDetails);
    void deleteRecord(Long id);
    List<MaintenanceRecordWithBLOBs> getRecordsByEquipmentId(String equipmentId);
}