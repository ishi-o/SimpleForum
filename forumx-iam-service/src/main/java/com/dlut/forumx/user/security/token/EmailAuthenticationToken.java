package com.dlut.forumx.user.security.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public class EmailAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;
	private Object credentials;

	public EmailAuthenticationToken(String username, String password) {
		super(null);
		this.principal = username;
		this.credentials = password;
		setAuthenticated(false);
	}

	public EmailAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(true);
		this.credentials = null;
	}

}
