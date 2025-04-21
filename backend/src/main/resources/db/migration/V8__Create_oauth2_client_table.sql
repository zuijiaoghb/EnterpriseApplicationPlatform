CREATE TABLE oauth2_client (
  client_id VARCHAR(50) PRIMARY KEY,
  client_name VARCHAR(100) NOT NULL COMMENT '客户端名称',
  client_secret VARCHAR(100) NOT NULL COMMENT '加密后的密钥',
  description VARCHAR(255) COMMENT '客户端描述',
  scopes VARCHAR(200) NOT NULL COMMENT '逗号分隔的权限范围',
  active BOOLEAN DEFAULT TRUE COMMENT '是否激活',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 初始化管理员客户端
INSERT INTO oauth2_client (client_id, client_name, client_secret, scopes) 
VALUES ('admin-client', '管理后台', '$2a$10$xVCH4IAZwRoaBtX8fRD4ReXUYvRqSOnzE4TqUog8X3dQg.8Q5ZQ6O', 'client.read,client.write,client.delete');