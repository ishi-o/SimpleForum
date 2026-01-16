package com.dlut.forumx.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
	private Long userId;
	private String username;
	private String token;

	public static LoginResponse loginResponse(Long userId, String username, String token) {
		return builder()
				.userId(userId)
				.username(username)
				.token(token)
				.build();
	}
}
