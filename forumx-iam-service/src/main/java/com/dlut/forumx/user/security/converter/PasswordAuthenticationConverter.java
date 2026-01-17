package com.dlut.forumx.user.security.converter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import com.dlut.forumx.user.security.token.PasswordAuthenticationToken;

import jakarta.servlet.http.HttpServletRequest;

public class PasswordAuthenticationConverter implements AuthenticationConverter {

	@Override
	public Authentication convert(HttpServletRequest request) {
		if (!"password".equals(request.getParameter("auth_type"))) {
			return null;
		}
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		return new PasswordAuthenticationToken(username, password);
	}

}
