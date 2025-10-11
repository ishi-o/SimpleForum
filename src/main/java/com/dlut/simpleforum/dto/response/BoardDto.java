package com.dlut.simpleforum.dto.response;

import java.time.LocalDateTime;

import com.dlut.simpleforum.entity.Board;

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

	private Long moderatorId;

	private LocalDateTime createdAt;

	public static BoardDto createBoardDto(Board board) {
		return builder()
				.bid(board.getBid())
				.name(board.getName())
				.description(board.getDescription())
				.moderatorId(board.getModerator().getUid())
				.createdAt(board.getCreatedAt())
				.build();
	}
}
