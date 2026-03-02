package com.dlut.forumx.commons.dto.user.response;

import lombok.Data;

@Data
public class UserSettingsDTO {
	private Long userId;
	private Integer privacyProfile;
	private Integer privacyPost;
	private Integer notificationLike;
	private Integer notificationComment;
	private Integer notificationFollow;
	private Integer notificationSystem;
	private Integer emailNotify;
	private String updateTime;
}
