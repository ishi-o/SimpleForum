package com.dlut.forumx.commons.dto.user.response;

import lombok.Data;

@Data
public class UserStatsDTO {
	private Long userId;
	private Integer followCount;
	private Integer fansCount;
	private Integer postCount;
	private Integer commentCount;
	private Integer likeCount;
	private Integer favoriteCount;
	private Integer viewCount;
}
