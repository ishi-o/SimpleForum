package com.dlut.forumx.commons.dto.user.response;

import lombok.Data;

@Data
public class HotTagDTO {
	private String tagName;
	private Integer userCount;
	private Integer postCount;
	private Integer weight;
}
