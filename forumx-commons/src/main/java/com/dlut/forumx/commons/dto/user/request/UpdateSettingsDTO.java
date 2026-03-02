package com.dlut.forumx.commons.dto.user.request;

import lombok.Data;

@Data
public class UpdateSettingsDTO {
	private Integer privacyProfile;
	private Integer privacyPost;
	private Integer notificationLike;
	private Integer notificationComment;
	private Integer notificationFollow;
	private Integer notificationSystem;
	private Integer emailNotify;
}
