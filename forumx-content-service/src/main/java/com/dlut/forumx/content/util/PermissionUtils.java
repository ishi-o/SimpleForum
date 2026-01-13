package com.dlut.forumx.content.util;

import com.dlut.forumx.content.entity.User.UserRole;

/**
 * @author Ishi_O
 * @since
 */
public class PermissionUtils {
	private PermissionUtils() {
	}

	private static void throwPermissionException() {
		throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.editor.no-permission", null));
	}

	public static void isRoleThenThrow(UserRole userRole, Object obj) {
		if (userRole.equals(obj)) {
			throwPermissionException();
		}
	}

	public static void isNotRoleThenThrow(UserRole userRole, Object obj) {
		if (!userRole.equals(obj)) {
			throwPermissionException();
		}
	}
}
