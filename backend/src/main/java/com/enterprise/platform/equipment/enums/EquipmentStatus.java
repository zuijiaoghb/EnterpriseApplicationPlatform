// "enum" 是 Java 中的关键字，不能用作包名的一部分，这里将其改为 enums
package com.enterprise.platform.equipment.enums;

public enum EquipmentStatus {
    ACTIVE, INACTIVE, MAINTENANCE, RUNNING; // 添加RUNNING状态
    
    public static EquipmentStatus fromString(String value) {
        try {
            return EquipmentStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ACTIVE; // 默认值
        }
    }
}