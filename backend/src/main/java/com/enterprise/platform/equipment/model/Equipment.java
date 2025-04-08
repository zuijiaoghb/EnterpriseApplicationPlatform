package com.enterprise.platform.equipment.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import com.enterprise.platform.equipment.enums.EquipmentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 添加主键生成策略
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "model", length = 50)
    private String model;

    @Enumerated(EnumType.STRING) // 添加枚举映射
    @Column(name = "status", columnDefinition = "ENUM('ACTIVE','INACTIVE','MAINTENANCE')") // 与数据库一致
    private EquipmentStatus status; // 改为枚举类型

    @Column(name = "qr_code", length = 100, nullable = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)  // 总是包含此字段
    private String qrCode;

    @Column(name = "last_maintenance")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonInclude(JsonInclude.Include.ALWAYS)  // 总是包含此字段
    private Date lastMaintenance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getStatus() {
        return status != null ? status.name() : null;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : EquipmentStatus.valueOf(status.trim());
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode == null ? null : qrCode.trim();
    }

    public Date getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(Date lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }
}
