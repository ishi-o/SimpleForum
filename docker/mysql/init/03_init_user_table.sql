USE user_db;

CREATE TABLE `users` (
    `user_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'User ID'
    , `username` VARCHAR(50) NOT NULL COMMENT 'Username'
    , `email` VARCHAR(100) NOT NULL COMMENT 'Email'
    , `phone` VARCHAR(20) DEFAULT NULL COMMENT 'Phone number'
    , `password` VARCHAR(255) NOT NULL COMMENT 'Encrypted password'
    , `nickname` VARCHAR(50) DEFAULT NULL COMMENT 'Nickname'
    , `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT 'Avatar URL'
    , `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-active, 2-locked'
    , `last_login_time` DATETIME DEFAULT NULL COMMENT 'Last login time'
    , `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time'
    , `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time'
    , `deleted_at` DATETIME DEFAULT NULL COMMENT 'Soft deletion timestamp'
    , PRIMARY KEY (`user_id`)
    , UNIQUE KEY `uk_username` (`username`)
    , UNIQUE KEY `uk_email` (`email`)
    , KEY `idx_phone` (`phone`)
    , KEY `idx_status` (`status`)
    , KEY `idx_created` (`created_at`)
) ENGINE
= InnoDB DEFAULT CHARSET
= utf8mb4 COLLATE
= utf8mb4_unicode_ci COMMENT
= 'Users master table';

CREATE TABLE `roles` (
    `role_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Role ID'
    , `role_code` VARCHAR(50) NOT NULL COMMENT 'Role code'
    , `role_name` VARCHAR(50) NOT NULL COMMENT 'Role name'
    , `description` VARCHAR(255) DEFAULT NULL COMMENT 'Role description'
    , `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time'
    , PRIMARY KEY (`role_id`)
    , UNIQUE KEY `uk_role_code` (`role_code`)
) COMMENT = 'Roles table';

CREATE TABLE `permissions` (
    `permission_id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Permission ID'
    , `permission_code` VARCHAR(100) NOT NULL COMMENT 'Permission code'
    , `permission_name` VARCHAR(50) NOT NULL COMMENT 'Permission name'
    , `permission_type` TINYINT NOT NULL COMMENT 'Permission type: 1-DATA, 2-FUNCTION, 3-API'
    , `parent_id` INT UNSIGNED DEFAULT 0 COMMENT 'Parent permission ID'
    , `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time'
    , `api_method` VARCHAR(10) NOT NULL COMMENT 'HTTP Method'
    , `api_path` VARCHAR(50) NOT NULL COMMENT 'Request URL path'
    , PRIMARY KEY (`permission_id`)
    , UNIQUE KEY `uk_permission_code` (`permission_code`)
    , KEY `idx_parent` (`parent_id`)
    , KEY `idx_type` (`permission_type`)
) COMMENT = 'Permissions table';

CREATE TABLE `user_roles` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Record ID'
    , `user_id` BIGINT UNSIGNED NOT NULL COMMENT 'User ID'
    , `role_id` INT UNSIGNED NOT NULL COMMENT 'Role ID'
    , `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time'
    , PRIMARY KEY (`id`)
    , UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
    , KEY `idx_role` (`role_id`)
    , FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
    , FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE
) COMMENT = 'User-roles relationship table';

CREATE TABLE `role_permissions` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Record ID'
    , `role_id` INT UNSIGNED NOT NULL COMMENT 'Role ID'
    , `permission_id` INT UNSIGNED NOT NULL COMMENT 'Permission ID'
    , `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time'
    , PRIMARY KEY (`id`)
    , UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
    , KEY `idx_permission` (`permission_id`)
    , FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE
    , FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`permission_id`) ON DELETE CASCADE
) COMMENT = 'Role-permissions relationship table';
