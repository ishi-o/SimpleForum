package com.dlut.forumx.commons.dto.user.response;

import lombok.Data;

@Data
public class UserProfileDTO {
	private Long id;
	private String username;
	private String nickname;
	private String avatar;
	private String bio;
	private Integer gender;
	private String birthday;
	private String location;
	private String website;
	private Integer followCount;
	private Integer fansCount;
	private Integer postCount;
	private Boolean isFollowed;
	private String registerTime;
}
