package com.dlut.simpleforum.common.session;

import java.util.UUID;

import com.dlut.simpleforum.common.enums.UserRole;
import com.dlut.simpleforum.entity.User;

import lombok.Builder;
import lombok.Data;

/**
 * @author Ishi_O
 * @since
 */
@Data
@Builder
public class SessionUser {
	private Long uid;

	private String username;

	private UserRole userRole;

	private String uuid;

	public static SessionUser createDefaultSessionUser() {
		String rdmUuid = "guest_" + UUID.randomUUID().toString();
		return SessionUser.builder()
				.uid(null)
				.username(rdmUuid)
				.userRole(UserRole.GUEST)
				.uuid(rdmUuid)
				.build();
	}

	public void userLogin(User user) {
		uid = user.getUid();
		username = user.getName();
		userRole = user.getRole();
	}

	public void userLogout() {
		uid = null;
		username = uuid;
		userRole = UserRole.GUEST;
	}

}
