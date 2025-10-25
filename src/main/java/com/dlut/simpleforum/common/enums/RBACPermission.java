package com.dlut.simpleforum.common.enums;

import lombok.Getter;

/**
 * @author Ishi_O
 * @since
 */
@Getter
public enum RBACPermission {
	BOARD_VIEW("board:view"),
	BOARD_DETAIL("board:detail"),
	BOARD_CREATE("board:create"),

	POST_VIEW("post:view"),
	POST_DETAIL("post:detail"),
	POST_CREATE("post:create"),

	COMMENT_VIEW("comment:view"),
	COMMENT_DETAIL("comment:detail"),
	COMMENT_CREATE("comment:create");

	private final String description;

	RBACPermission(String decription) {
		this.description = decription;
	}
}
