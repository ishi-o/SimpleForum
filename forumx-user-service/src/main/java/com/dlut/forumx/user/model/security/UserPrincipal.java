package com.dlut.forumx.user.model.security;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPrincipal implements UserDetails {
	private Long userId;
	private String username;
	private String password;
	private List<Authority> authorities;

}
