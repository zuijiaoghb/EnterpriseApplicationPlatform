package com.enterprise.platform.equipment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.platform.equipment.enums.EquipmentStatus;
import com.enterprise.platform.equipment.model.Equipment;
import com.enterprise.platform.equipment.repository.EquipmentRepository;
import com.enterprise.platform.equipment.service.EquipmentService;

import java.util.List;

@Service
@Transactional(transactionManager = "mysqlTransactionManager")
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    @Override
    public Equipment getEquipmentById(String id) {
        return equipmentRepository.findById(id).orElseThrow();
    }

    @Override
    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment updateEquipment(String id, Equipment equipment) {
        equipment.setId(id);
        return equipmentRepository.save(equipment);
    }

    @Override
    public void deleteEquipment(String id) {
        equipmentRepository.deleteById(id);
    }

    @Override
    public List<Equipment> getEquipmentsByStatus(String status) {
        // 验证输入
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("状态参数不能为空");
        }
        
        EquipmentStatus equipmentStatus;
        try {
            equipmentStatus = EquipmentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的设备状态: " + status);
        }
        
        return equipmentRepository.findByStatus(equipmentStatus);
    }

    @Override
    public List<Equipment> getEquipmentsNeedMaintenance() {
        return equipmentRepository.findEquipmentsNeedMaintenance();
    }
}