package com.dlut.simpleforum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ishi_O
 * @since
 */
@AllArgsConstructor
@Getter
public enum ABACPermission {
	BOARD_EDIT(ResourceType.BOARD),

	POST_EDIT(ResourceType.POST);

	@Getter
	@AllArgsConstructor
	public static enum ResourceType {
		BOARD,
		POST,
		COMMENT;
	}

	private ResourceType resourceType;
}
