package com.dlut.forumx.common.content.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * @author Ishi_O
 * @since
 */
@Data
@Builder
public class BoardDto {
	private Long bid;

	private String name;

	private String description;

	private String iconUrl;
	private String coverUrl;
	private Long moderatorId;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
