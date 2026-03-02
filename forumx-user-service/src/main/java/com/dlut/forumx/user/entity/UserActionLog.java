package com.dlut.forumx.user.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserActionLog {
	private Long id;
	private Long userId;
	private String actionType;
	private String targetId;
	private String targetType;
	private String extraData;
	private String ipAddress;
	private String userAgent;
	private LocalDateTime createTime;
}
