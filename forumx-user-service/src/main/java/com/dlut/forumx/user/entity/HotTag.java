package com.dlut.forumx.user.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HotTag {
	private String tagName;
	private Integer userCount;
	private Integer postCount;
	private Integer weight;
	private LocalDateTime updateTime;
}
