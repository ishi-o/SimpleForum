package com.dlut.forumx.user.security.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class PasswordAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;
	private Object credentials;

	public PasswordAuthenticationToken(String username, String password) {
		super(null);
		this.principal = username;
		this.credentials = password;
		setAuthenticated(false);
	}

	public PasswordAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

}
