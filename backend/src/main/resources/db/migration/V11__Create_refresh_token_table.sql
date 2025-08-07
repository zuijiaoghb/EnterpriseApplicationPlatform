-- 创建刷新令牌表
CREATE TABLE sys_refresh_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    revoked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
);

-- 添加索引以提高查询性能
CREATE INDEX idx_refresh_token_user_id ON sys_refresh_token(user_id);
CREATE INDEX idx_refresh_token_token ON sys_refresh_token(token);