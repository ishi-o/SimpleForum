USE user_db;

-- 用户主表
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `keycloak_id` VARCHAR(36) NOT NULL COMMENT 'Keycloak用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT '' COMMENT '手机号',
    `nickname` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT '' COMMENT '头像URL',
    `bio` VARCHAR(500) DEFAULT '' COMMENT '个人简介',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 0未知 1男 2女',
    `birthday` DATE DEFAULT NULL COMMENT '出生日期',
    `location` VARCHAR(100) DEFAULT '' COMMENT '所在地',
    `website` VARCHAR(255) DEFAULT '' COMMENT '个人网站',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0禁用 1正常',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(45) DEFAULT '' COMMENT '最后登录IP',
    `register_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY `uk_keycloak_id` (`keycloak_id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_nickname` (`nickname`),
    KEY `idx_status` (`status`),
    KEY `idx_register_time` (`register_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户主表';

-- 用户统计表
CREATE TABLE `user_stats` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `follow_count` INT NOT NULL DEFAULT 0 COMMENT '关注数',
    `fans_count` INT NOT NULL DEFAULT 0 COMMENT '粉丝数',
    `post_count` INT NOT NULL DEFAULT 0 COMMENT '发帖数',
    `comment_count` INT NOT NULL DEFAULT 0 COMMENT '评论数',
    `like_count` INT NOT NULL DEFAULT 0 COMMENT '获赞数',
    `favorite_count` INT NOT NULL DEFAULT 0 COMMENT '收藏数',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '主页浏览量',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    PRIMARY KEY (`user_id`),
    KEY `idx_follow_count` (`follow_count`),
    KEY `idx_fans_count` (`fans_count`),
    KEY `idx_post_count` (`post_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户统计表';

-- 关注关系表
CREATE TABLE `user_follow` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    `follower_id` BIGINT NOT NULL COMMENT '关注者ID',
    `followed_id` BIGINT NOT NULL COMMENT '被关注者ID',
    `follow_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY `uk_follower_followed` (`follower_id`, `followed_id`),
    KEY `idx_follower_id` (`follower_id`, `follow_time`),
    KEY `idx_followed_id` (`followed_id`, `follow_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关注关系表';

-- 用户标签表
CREATE TABLE `user_tag` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `tag_source` TINYINT NOT NULL DEFAULT 1 COMMENT '标签来源 1自选 2系统 3行为',
    `weight` INT NOT NULL DEFAULT 1 COMMENT '标签权重',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY `uk_user_tag` (`user_id`, `tag_name`),
    KEY `idx_tag_name` (`tag_name`, `weight`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户标签表';

-- 热门标签表
CREATE TABLE `hot_tag` (
    `tag_name` VARCHAR(50) PRIMARY KEY COMMENT '标签名称',
    `user_count` INT NOT NULL DEFAULT 0 COMMENT '使用人数',
    `post_count` INT NOT NULL DEFAULT 0 COMMENT '关联帖子数',
    `weight` INT NOT NULL DEFAULT 0 COMMENT '综合权重',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    KEY `idx_weight` (`weight`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='热门标签表';

-- 用户设置表
CREATE TABLE `user_settings` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `privacy_profile` TINYINT NOT NULL DEFAULT 0 COMMENT '资料可见性 0公开 1粉丝 2私密',
    `privacy_post` TINYINT NOT NULL DEFAULT 0 COMMENT '帖子可见性 0公开 1粉丝 2私密',
    `notification_like` TINYINT NOT NULL DEFAULT 1 COMMENT '点赞通知 0关 1开',
    `notification_comment` TINYINT NOT NULL DEFAULT 1 COMMENT '评论通知 0关 1开',
    `notification_follow` TINYINT NOT NULL DEFAULT 1 COMMENT '关注通知 0关 1开',
    `notification_system` TINYINT NOT NULL DEFAULT 1 COMMENT '系统通知 0关 1开',
    `email_notify` TINYINT NOT NULL DEFAULT 0 COMMENT '邮件通知 0关 1开',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户设置表';

-- 用户操作日志表
CREATE TABLE `user_action_log` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `action_type` VARCHAR(30) NOT NULL COMMENT '操作类型',
    `target_id` VARCHAR(50) DEFAULT '' COMMENT '目标ID',
    `target_type` VARCHAR(20) DEFAULT '' COMMENT '目标类型',
    `extra_data` JSON DEFAULT NULL COMMENT '额外数据',
    `ip_address` VARCHAR(45) DEFAULT '' COMMENT 'IP地址',
    `user_agent` VARCHAR(500) DEFAULT '' COMMENT '用户代理',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    KEY `idx_user_id` (`user_id`, `create_time`),
    KEY `idx_action_type` (`action_type`, `create_time`),
    KEY `idx_target` (`target_id`, `target_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户操作日志表';
