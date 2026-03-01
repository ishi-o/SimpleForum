USE user_db;

CREATE TABLE IF NOT EXISTS `local_users` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '本地用户ID（业务主键）',
    `keycloak_id` VARCHAR(36) NOT NULL COMMENT 'Keycloak用户ID (sub)',
    `username` VARCHAR(100) NOT NULL COMMENT '用户名 (preferred_username)',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL (picture)',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 1-正常 0-禁用',
    `version` BIGINT NOT NULL DEFAULT 0 COMMENT '版本号',
    `last_sync_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后同步时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_keycloak_id` (`keycloak_id`),
    INDEX `idx_username` (`username`),
    INDEX `idx_status` (`status`),
    INDEX `idx_last_sync` (`last_sync_at`),
    INDEX `idx_updated_at` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='本地用户表';
