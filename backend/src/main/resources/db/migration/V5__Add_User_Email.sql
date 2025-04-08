ALTER TABLE sys_user ADD COLUMN email VARCHAR(255) NOT NULL AFTER username;
CREATE UNIQUE INDEX idx_email ON sys_user(email);