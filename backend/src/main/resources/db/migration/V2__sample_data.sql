-- 确保先有设备数据
INSERT INTO equipment (id, name, model, status) VALUES
('E001', '数控机床', 'CNC-1000', 'RUNNING'),
('E002', '激光切割机', 'LC-2000', 'RUNNING'),
('E003', '3D打印机', '3DP-500', 'MAINTENANCE');

-- 添加维修记录数据
INSERT INTO maintenance_record (equipment_id, reporter, location, images) VALUES
('E001', '张三', ST_GeomFromText('POINT(116.404 39.915)'), '["/images/repair1.jpg", "/images/repair2.jpg"]'),
('E002', '李四', ST_GeomFromText('POINT(121.474 31.230)'), '["/images/fault1.jpg"]'),
('E003', '王五', ST_GeomFromText('POINT(113.264 23.129)'), '["/images/maintenance1.jpg", "/images/maintenance2.jpg"]');