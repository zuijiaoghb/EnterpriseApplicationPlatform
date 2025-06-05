package com.enterprise.platform.equipment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.platform.equipment.model.MaintenanceRecordWithBLOBs;
import com.enterprise.platform.equipment.repository.MaintenanceRecordRepository;
import com.enterprise.platform.equipment.service.MaintenanceRecordService;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional(transactionManager = "mysqlTransactionManager")
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    @PersistenceContext(unitName = "mysqlEntityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Override
    public MaintenanceRecordWithBLOBs createRecord(MaintenanceRecordWithBLOBs record) {
        return maintenanceRecordRepository.save(record);
    }

    @Override
    public List<MaintenanceRecordWithBLOBs> getAllRecords() {
        return maintenanceRecordRepository.findAll();
    }

    @Override
    public MaintenanceRecordWithBLOBs getRecordById(Long id) {
        return maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
    }

    @Override
    public MaintenanceRecordWithBLOBs updateRecord(Long id, MaintenanceRecordWithBLOBs recordDetails) {
        MaintenanceRecordWithBLOBs record = getRecordById(id);
        record.setEquipmentId(recordDetails.getEquipmentId());
        record.setReporter(recordDetails.getReporter());
        record.setLocation(recordDetails.getLocation());
        record.setImages(recordDetails.getImages());
        return maintenanceRecordRepository.save(record);
    }

    @Override
    public void deleteRecord(Long id) {
        maintenanceRecordRepository.deleteById(id);
    }

    @Override
    public List<MaintenanceRecordWithBLOBs> getRecordsByEquipmentId(String equipmentId) {        
        return entityManager.createNamedQuery("MaintenanceRecordWithBLOBs.findByEquipmentId", MaintenanceRecordWithBLOBs.class)
            .setParameter("equipmentId", equipmentId)
            .getResultList();
    }
}