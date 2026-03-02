package com.dlut.forumx.commons.dto.user.request;

import java.util.List;

import lombok.Data;

@Data
public class FollowStatusRequestDTO {
	private List<Long> targetUserIds;
}
