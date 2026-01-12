USE content_db;

CREATE TABLE IF NOT EXISTS users (
    uid BIGINT NOT NULL AUTO_INCREMENT COMMENT 'User ID'
    , name VARCHAR(50) NOT NULL COMMENT 'Username'
    , password VARCHAR(128) NOT NULL COMMENT 'Password'
    , role VARCHAR(255) NOT NULL COMMENT 'User role (enum)'
    , status VARCHAR(255) NOT NULL COMMENT 'User status (enum)'
    , created_at DATETIME NOT NULL COMMENT 'Creation time'
    , PRIMARY KEY (uid)
    , UNIQUE KEY user_uniq_name (name)
    , CONSTRAINT user_chk_name CHECK (name REGEXP '^[0-9a-zA-Z_\u4e00-\u9fff]{2,10}$')
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'User table';

CREATE TABLE IF NOT EXISTS boards (
    bid BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Board ID'
    , name VARCHAR(20) NOT NULL COMMENT 'Board name'
    , description VARCHAR(50) DEFAULT NULL COMMENT 'Board description'
    , moderator_id BIGINT NOT NULL COMMENT 'Moderator ID (associate with users.uid)'
    , created_at DATETIME NOT NULL COMMENT 'Creation time'
    , PRIMARY KEY (bid)
    , UNIQUE KEY board_uniq_name (name)
    , CONSTRAINT board_chk_name CHECK (name REGEXP '^[0-9a-zA-Z\u4e00-\u9fff]{2,16}$')
    , CONSTRAINT board_chk_descrip CHECK (LENGTH(description) <= 40)
    , CONSTRAINT fk_board_moderator FOREIGN KEY (moderator_id) REFERENCES users (
        uid
    ) ON DELETE RESTRICT
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Board table';

CREATE TABLE IF NOT EXISTS posts (
    pid BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Post ID'
    , title VARCHAR(255) NOT NULL COMMENT 'Post title'
    , content LONGTEXT DEFAULT NULL COMMENT 'Post content (long text)'
    , created_at DATETIME NOT NULL COMMENT 'Creation time'
    , pinned BOOLEAN DEFAULT FALSE COMMENT 'Whether to pin'
    , likes INT DEFAULT 0 COMMENT 'Like count'
    , dislikes INT DEFAULT 0 COMMENT 'Dislike count'
    , author_id BIGINT NOT NULL COMMENT 'Author ID (associate with users.uid)'
    , board_id BIGINT NOT NULL COMMENT 'Board ID (associate with boards.bid)'
    , PRIMARY KEY (pid)
    , CONSTRAINT post_chk_title CHECK (title REGEXP '^[0-9a-zA-Z\u4e00-\u9fff]{2,10}$')
    , CONSTRAINT post_chk_content CHECK (LENGTH(content) <= 4096)
    , CONSTRAINT fk_post_author FOREIGN KEY (author_id) REFERENCES users (uid) ON DELETE RESTRICT
    , CONSTRAINT fk_post_board FOREIGN KEY (board_id) REFERENCES boards (bid) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Post table';

CREATE TABLE IF NOT EXISTS comments (
    cid BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Comment ID'
    , content VARCHAR(255) NOT NULL COMMENT 'Comment content'
    , created_at DATETIME NOT NULL COMMENT 'Creation time'
    , likes INT DEFAULT 0 COMMENT 'Like count'
    , dislikes INT DEFAULT 0 COMMENT 'Dislike count'
    , author_id BIGINT NOT NULL COMMENT 'Comment author ID (associate with users.uid)'
    , PRIMARY KEY (cid)
    , CONSTRAINT comment_chk_content CHECK (LENGTH(content) BETWEEN 1 AND 128)
    , CONSTRAINT comment_chk_positive CHECK (likes >= 0 AND dislikes >= 0)
    , CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (uid) ON DELETE RESTRICT
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Comment base table';

CREATE TABLE IF NOT EXISTS main_comments (
    cid BIGINT NOT NULL COMMENT 'Comment ID (associate with comments.cid)'
    , post_id BIGINT NOT NULL COMMENT 'Associated post ID (associate with posts.pid)'
    , PRIMARY KEY (cid)
    , CONSTRAINT fk_main_comment_base FOREIGN KEY (cid) REFERENCES comments (cid) ON DELETE CASCADE
    , CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts (pid) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Main comment table (associate with posts)';

CREATE TABLE IF NOT EXISTS sub_comments (
    cid BIGINT NOT NULL COMMENT 'Comment ID (associate with comments.cid)'
    , parent_cid BIGINT NOT NULL COMMENT 'Parent comment ID (associate with main_comments.cid)'
    , target_cid BIGINT DEFAULT NULL COMMENT 'Reply target sub-comment ID (associate with sub_comments.cid)'
    , PRIMARY KEY (cid)
    , CONSTRAINT fk_sub_comment_base FOREIGN KEY (cid) REFERENCES comments (cid) ON DELETE CASCADE
    , CONSTRAINT fk_subcmt_maincmt FOREIGN KEY (parent_cid) REFERENCES main_comments (
        cid
    ) ON DELETE CASCADE
    , CONSTRAINT fk_subcmt_subcmt FOREIGN KEY (target_cid) REFERENCES sub_comments (
        cid
    ) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Sub-comment table (reply to comments)';

CREATE TABLE IF NOT EXISTS `undo_log` (
    `branch_id` BIGINT NOT NULL COMMENT '分支事务ID'
    , `xid` VARCHAR(128) NOT NULL COMMENT '全局事务ID'
    , `context` VARCHAR(128) NOT NULL COMMENT '上下文信息，格式：serializer=jackson'
    , `rollback_info` LONGBLOB NOT NULL COMMENT '回滚日志（序列化后的业务数据）'
    , `log_status` INT NOT NULL COMMENT '日志状态：0-正常，1-全局提交，2-全局回滚，3-分支提交'
    , `log_created` DATETIME NOT NULL COMMENT '日志创建时间'
    , `log_modified` DATETIME NOT NULL COMMENT '日志修改时间'
    , PRIMARY KEY (`branch_id`)
    , UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'Seata AT 模式回滚日志表';

