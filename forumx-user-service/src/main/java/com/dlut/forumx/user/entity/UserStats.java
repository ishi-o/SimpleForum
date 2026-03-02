package com.dlut.forumx.user.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserStats {
	private Long userId;
	private Integer followCount;
	private Integer fansCount;
	private Integer postCount;
	private Integer commentCount;
	private Integer likeCount;
	private Integer favoriteCount;
	private Integer viewCount;
	private LocalDateTime updateTime;
}
