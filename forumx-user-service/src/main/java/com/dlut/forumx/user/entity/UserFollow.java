package com.dlut.forumx.user.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserFollow {
	private Long id;
	private Long followerId;
	private Long followedId;
	private LocalDateTime followTime;
	private LocalDateTime updateTime;
}
