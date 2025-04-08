package com.enterprise.platform.equipment.model;

import org.locationtech.jts.geom.Point;

import com.enterprise.platform.system.util.JsonConverter;

import jakarta.persistence.*;

@Entity
@Table(name = "maintenance_record")  // 替换为实际表名
@NamedQuery(
    name = "MaintenanceRecordWithBLOBs.findByEquipmentId",
    query = "SELECT r FROM MaintenanceRecordWithBLOBs r WHERE r.equipmentId = :equipmentId"
)
public class MaintenanceRecordWithBLOBs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String reporter;

    private Point location;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonConverter.class)
    private String images;  // 或其他JSON字段

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter == null ? null : reporter.trim();
    }

    public Point getLocation() {  // 同步修改返回类型
        return location;
    }

    public void setLocation(Point location) {  // 参数类型同步修改
        this.location = location;
    }

    public String getImages() {
        return images == null ? "[]" : images;
    }

    public void setImages(String images) {
        this.images = (images == null || images.trim().isEmpty()) ? "[]" : images;
    }

    @Column(name = "equipment_id") // 确保与数据库列名一致
    private String equipmentId;

    // 添加getter/setter
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }
}