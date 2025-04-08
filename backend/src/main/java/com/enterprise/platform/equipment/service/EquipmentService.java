package com.enterprise.platform.equipment.service;

import java.util.List;

import com.enterprise.platform.equipment.model.Equipment;

public interface EquipmentService {
    List<Equipment> getAllEquipments();
    Equipment getEquipmentById(String id);
    Equipment createEquipment(Equipment equipment);
    Equipment updateEquipment(String id, Equipment equipment);
    void deleteEquipment(String id);
    List<Equipment> getEquipmentsByStatus(String status);
    List<Equipment> getEquipmentsNeedMaintenance();
}