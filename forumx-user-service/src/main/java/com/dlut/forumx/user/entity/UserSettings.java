package com.dlut.forumx.user.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserSettings {
	private Long userId;
	private Integer privacyProfile;
	private Integer privacyPost;
	private Integer notificationLike;
	private Integer notificationComment;
	private Integer notificationFollow;
	private Integer notificationSystem;
	private Integer emailNotify;
	private LocalDateTime updateTime;
}
