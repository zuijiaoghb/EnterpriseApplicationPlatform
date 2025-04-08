-- 设备主表
CREATE TABLE equipment (
  id VARCHAR(36) PRIMARY KEY COMMENT '设备唯一标识',
  name VARCHAR(100) NOT NULL COMMENT '设备名称',
  model VARCHAR(50) COMMENT '设备型号',
  status ENUM('RUNNING', 'MAINTENANCE', 'FAULT') DEFAULT 'RUNNING',
  qr_code VARCHAR(200) COMMENT '二维码存储路径',
  last_maintenance DATETIME COMMENT '最近维保时间',
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 维修记录表（包含移动端上报功能字段）
CREATE TABLE maintenance_record (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  equipment_id VARCHAR(36) NOT NULL,
  reporter VARCHAR(50) COMMENT '移动端上报人员',
  location POINT COMMENT 'GIS地理位置',
  images JSON COMMENT '现场照片URL数组',
  FOREIGN KEY (equipment_id) REFERENCES equipment(id)
);