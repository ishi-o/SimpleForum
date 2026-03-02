package com.dlut.forumx.user.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserTag {
	private Long id;
	private Long userId;
	private String tagName;
	private Integer tagSource;
	private Integer weight;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
