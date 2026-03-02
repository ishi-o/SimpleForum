package com.dlut.forumx.commons.dto.user.response;

import lombok.Data;

@Data
public class UserFollowDTO {
	private Long userId;
	private String username;
	private String nickname;
	private String avatar;
	private String bio;
	private Boolean isMutual;
	private String followTime;
}
