package com.enterprise.platform.equipment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.enterprise.platform.equipment.enums.EquipmentStatus;
import com.enterprise.platform.equipment.model.Equipment;
import com.enterprise.platform.equipment.service.EquipmentService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {
private static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);

    @Autowired
    private EquipmentService equipmentService;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SBGL', 'ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipments() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        logger.info("当前用户: {}, 角色: {}", auth.getName(), auth.getAuthorities());
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable String id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_BGL')")
    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipmentService.createEquipment(equipment));
    }
 
    @PreAuthorize("hasRole('ROLE_ADMIN','ROLE_SBGL')")
    @PutMapping("/{id}")
    public ResponseEntity<Equipment> updateEquipment(
            @PathVariable String id, 
            @RequestBody Equipment equipment) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, equipment));
    }
 
    @PreAuthorize("hasRole('ROLE_ADMIN','ROLE_SBGL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable String id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
 
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SBGL', 'ROLE_USER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Equipment>> getByStatus(@PathVariable String status) {
        List<Equipment> equipments = equipmentService.getEquipmentsByStatus(status);
        return ResponseEntity.ok(equipments);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SBGL', 'ROLE_USER')")
    @GetMapping("/last-maintenance")
    public ResponseEntity<List<Equipment>> getEquipmentsNeedMaintenance() {
        return ResponseEntity.ok(equipmentService.getEquipmentsNeedMaintenance());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SBGL', 'ROLE_USER')")
    @GetMapping("/statuses")
    public ResponseEntity<EquipmentStatus[]> getAllStatuses() {
        return ResponseEntity.ok(EquipmentStatus.values());
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Controller is reachable");
    }
}