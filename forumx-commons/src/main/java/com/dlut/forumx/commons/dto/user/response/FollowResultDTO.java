package com.dlut.forumx.commons.dto.user.response;

import lombok.Data;

@Data
public class FollowResultDTO {
	private Boolean success;
	private Boolean isFollowing;
	private Integer followCount;
	private Integer fansCount;
}
