package com.dlut.forumx.common.content.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * @author Ishi_O
 * @since
 */
@Data
@Builder
public class PostDto {
	private Long pid;

	private String title;

	private Map<String, Object> content;

	private LocalDateTime createdAt;

	private Long boardId;

	private UserInfoDto author;

	private Boolean isPinned;

	private Integer likeCount;

	private Boolean liked;

	private Integer viewCount;

	private List<String> tags;
}
