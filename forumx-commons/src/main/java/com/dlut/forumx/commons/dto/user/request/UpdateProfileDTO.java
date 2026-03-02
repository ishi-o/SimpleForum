package com.dlut.forumx.commons.dto.user.request;

import lombok.Data;

@Data
public class UpdateProfileDTO {
	private String nickname;
	private String bio;
	private Integer gender;
	private String birthday;
	private String location;
	private String website;
	private String phone;
}
