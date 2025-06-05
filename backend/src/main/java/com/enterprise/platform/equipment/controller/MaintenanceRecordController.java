package com.enterprise.platform.equipment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.enterprise.platform.equipment.model.MaintenanceRecordWithBLOBs;
import com.enterprise.platform.equipment.service.MaintenanceRecordService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional; // 添加事务注解的导入语句
import java.util.Collections;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {
// 引入LoggerFactory和Logger类
private static final Logger logger = LoggerFactory.getLogger(MaintenanceRecordController.class);

    @Autowired
    private MaintenanceRecordService maintenanceRecordService;

    @PostMapping
    public ResponseEntity<MaintenanceRecordWithBLOBs> createRecord(
            @RequestBody MaintenanceRecordWithBLOBs record) {
        MaintenanceRecordWithBLOBs savedRecord = maintenanceRecordService.createRecord(record);
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceRecordWithBLOBs>> getAllRecords() {
        try {
            List<MaintenanceRecordWithBLOBs> records = maintenanceRecordService.getAllRecords();
            records.forEach(record -> {
                if(record.getImages() == null) {
                    record.setImages("[]");
                }
            });
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            logger.error("获取维修记录失败", e);
            return ResponseEntity.internalServerError()
                .body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecordWithBLOBs> getRecordById(
            @PathVariable Long id) {
        MaintenanceRecordWithBLOBs record = maintenanceRecordService.getRecordById(id);
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecordWithBLOBs> updateRecord(
            @PathVariable Long id,
            @RequestBody MaintenanceRecordWithBLOBs recordDetails) {
        MaintenanceRecordWithBLOBs updatedRecord = maintenanceRecordService.updateRecord(id, recordDetails);
        return ResponseEntity.ok(updatedRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long id) {
        maintenanceRecordService.deleteRecord(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/equipment/{equipmentId}")
    @Transactional(transactionManager = "mysqlTransactionManager")  // 添加事务注解
    public ResponseEntity<List<MaintenanceRecordWithBLOBs>> getRecordsByEquipmentId(
            @PathVariable String equipmentId) {
        try {
            List<MaintenanceRecordWithBLOBs> records = maintenanceRecordService.getRecordsByEquipmentId(equipmentId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            logger.error("根据设备ID查询维修记录失败", e);
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "查询失败: " + e.getMessage()
            );
        }
    }
}