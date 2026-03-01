package com.dlut.forumx.content.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Like {
	private Long lid;
	private Long userId;
	private Integer targetType;
	private Long targetId;
	private LocalDateTime createdAt;
}
