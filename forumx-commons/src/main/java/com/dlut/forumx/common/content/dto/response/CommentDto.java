package com.dlut.forumx.common.content.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
	private Long cid;
	private String content;

	private UserInfoDto author;

	private UserInfoDto replyToUser;

	private Integer likeCount;
	private Boolean liked;

	private LocalDateTime createdAt;

	private Long parentId;
	private List<CommentDto> replies;
}
