package com.dlut.simpleforum.common.enums;

import java.util.List;

import com.dlut.simpleforum.utils.MessageSourceUtils;

import lombok.Getter;

/**
 * @author Ishi_O
 * @since
 */
@Getter
public enum UserRole {
	USER(List.of(RBACPermission.values())
			.stream()
			.toList()),
	GUEST(List.of(RBACPermission.values())
			.stream()
			.takeWhile((permission) -> {
				String description = permission.getDescription();
				return description.endsWith("view");
			})
			.toList()),
	BANNED(List.of());

	private final List<RBACPermission> permissions;

	UserRole(List<RBACPermission> permissions) {
		this.permissions = permissions;
	}

	private void throwPermissionException() {
		throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.editor.no-permission", null));
	}

	public void checkPermission(RBACPermission... targets) {
		if (!hasPermission(targets)) {
			throwPermissionException();
		}
	}

	public boolean hasPermission(RBACPermission... targets) {
		return permissions.containsAll(List.of(targets));
	}
}
