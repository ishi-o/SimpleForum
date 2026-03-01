USE content_db;

CREATE TABLE IF NOT EXISTS `boards` (
    `bid` BIGINT NOT NULL AUTO_INCREMENT COMMENT '板块ID',
    `name` VARCHAR(50) NOT NULL COMMENT '板块名称',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '板块描述',
    `icon_url` VARCHAR(500) DEFAULT NULL COMMENT '板块图标URL',
    `cover_url` VARCHAR(500) DEFAULT NULL COMMENT '板块封面URL',
    `moderator_id` BIGINT NOT NULL COMMENT '版主本地用户ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`bid`),
    UNIQUE KEY `uk_board_name` (`name`),
    INDEX `idx_board_moderator` (`moderator_id`),
    CONSTRAINT `chk_board_name` CHECK (CHAR_LENGTH(`name`) BETWEEN 2 AND 50),
    CONSTRAINT `fk_board_moderator` FOREIGN KEY (`moderator_id`) REFERENCES `local_users`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='板块表';

CREATE TABLE IF NOT EXISTS `posts` (
    `pid` BIGINT NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
    `title` VARCHAR(200) NOT NULL COMMENT '帖子标题',
    `content` JSON NOT NULL COMMENT '帖子内容(JSON格式)',
    `author_id` BIGINT NOT NULL COMMENT '作者本地用户ID',
    `board_id` BIGINT NOT NULL COMMENT '所属板块ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1-已发布 2-草稿 3-审核中 4-隐藏',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览数',
    `is_pinned` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否置顶:0-否 1-是',
    `is_essence` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否精华:0-否 1-是',
    `tags` JSON DEFAULT NULL COMMENT '标签数组',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`pid`),
    INDEX `idx_post_author` (`author_id`),
    INDEX `idx_post_board` (`board_id`),
    INDEX `idx_post_status` (`status`),
    INDEX `idx_post_pinned` (`is_pinned` DESC, `created_at` DESC),
    INDEX `idx_post_essence` (`is_essence` DESC, `created_at` DESC),
    INDEX `idx_post_latest` (`created_at` DESC),
    CONSTRAINT `fk_post_board` FOREIGN KEY (`board_id`) REFERENCES `boards`(`bid`) ON DELETE RESTRICT,
    CONSTRAINT `fk_post_author` FOREIGN KEY (`author_id`) REFERENCES `local_users`(`id`) ON DELETE RESTRICT,
    CONSTRAINT `chk_post_title` CHECK (CHAR_LENGTH(`title`) BETWEEN 2 AND 200),
    CONSTRAINT `chk_post_status` CHECK (`status` BETWEEN 1 AND 4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

CREATE TABLE IF NOT EXISTS `comments` (
    `cid` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `author_id` BIGINT NOT NULL COMMENT '作者本地用户ID',
    `post_id` BIGINT NOT NULL COMMENT '所属帖子ID',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID',
    `reply_to_user_id` BIGINT DEFAULT NULL COMMENT '回复目标本地用户ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1-正常 2-隐藏 3-删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`cid`),
    INDEX `idx_comment_post` (`post_id`),
    INDEX `idx_comment_author` (`author_id`),
    INDEX `idx_comment_parent` (`parent_id`),
    INDEX `idx_comment_status` (`status`),
    INDEX `idx_comment_latest` (`created_at` DESC),
    CONSTRAINT `fk_comment_post` FOREIGN KEY (`post_id`) REFERENCES `posts`(`pid`) ON DELETE CASCADE,
    CONSTRAINT `fk_comment_parent` FOREIGN KEY (`parent_id`) REFERENCES `comments`(`cid`) ON DELETE CASCADE,
    CONSTRAINT `fk_comment_author` FOREIGN KEY (`author_id`) REFERENCES `local_users`(`id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_comment_reply_to` FOREIGN KEY (`reply_to_user_id`) REFERENCES `local_users`(`id`) ON DELETE RESTRICT,
    CONSTRAINT `chk_comment_content` CHECK (CHAR_LENGTH(`content`) BETWEEN 1 AND 5000),
    CONSTRAINT `chk_comment_status` CHECK (`status` BETWEEN 1 AND 3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

CREATE TABLE IF NOT EXISTS `attachments` (
    `aid` BIGINT NOT NULL AUTO_INCREMENT COMMENT '附件ID',
    `post_id` BIGINT NOT NULL COMMENT '所属帖子ID',
    `object_key` VARCHAR(500) NOT NULL COMMENT 'MinIO对象键',
    `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_size` INT NOT NULL DEFAULT 0 COMMENT '文件大小(字节)',
    `mime_type` VARCHAR(100) NOT NULL COMMENT 'MIME类型',
    `file_type` TINYINT NOT NULL DEFAULT 1 COMMENT '文件类型:1-图片 2-视频 3-音频 4-文档',
    `width` INT DEFAULT NULL COMMENT '图片/视频宽度(像素)',
    `height` INT DEFAULT NULL COMMENT '图片/视频高度(像素)',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`aid`),
    UNIQUE KEY `uk_attachment_object` (`object_key`),
    INDEX `idx_attachment_post` (`post_id`),
    INDEX `idx_attachment_type` (`file_type`),
    INDEX `idx_attachment_sort` (`post_id`, `sort_order`),
    CONSTRAINT `fk_attachment_post` FOREIGN KEY (`post_id`) REFERENCES `posts`(`pid`) ON DELETE CASCADE,
    CONSTRAINT `chk_attachment_size` CHECK (`file_size` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件表';

CREATE TABLE IF NOT EXISTS `likes` (
    `lid` BIGINT NOT NULL AUTO_INCREMENT COMMENT '点赞记录ID',
    `user_id` BIGINT NOT NULL COMMENT '本地用户ID',
    `target_type` TINYINT NOT NULL COMMENT '目标类型:1-帖子 2-评论',
    `target_id` BIGINT NOT NULL COMMENT '目标ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`lid`),
    UNIQUE KEY `uk_like_user_target` (`user_id`, `target_type`, `target_id`),
    INDEX `idx_like_target` (`target_type`, `target_id`),
    INDEX `idx_like_user` (`user_id`),
    CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `local_users`(`id`) ON DELETE CASCADE,
    CONSTRAINT `chk_like_target_type` CHECK (`target_type` IN (1, 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞记录表';
