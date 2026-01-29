package com.dlut.forumx.user.model.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPrincipal implements UserDetails {
	private Long userId;
	private String username;
	private String password;
	private List<? extends GrantedAuthority> authorities;

	public static UserPrincipal createUserPrincipal(Long userId, String username, String password,
			List<? extends GrantedAuthority> authorities) {
		return builder()
				.userId(userId)
				.username(username)
				.authorities(authorities)
				.build();
	}
}
