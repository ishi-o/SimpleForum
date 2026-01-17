package com.dlut.forumx.user.security.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;

import jakarta.servlet.http.HttpServletRequest;

public class CodeRequestAuthenticationConverter implements AuthenticationConverter {

	@Override
	public Authentication convert(HttpServletRequest request) {
		if (!"/oauth2/authorize".equals(request.getServletPath()) ||
				!"GET".equalsIgnoreCase(request.getMethod())) {
			return null;
		}

		String clientId = request.getParameter("client_id");
		String responseType = request.getParameter("response_type");
		if (clientId == null || !"code".equals(responseType)) {
			return null;
		}

		String redirectUri = request.getParameter("redirect_uri");
		String state = request.getParameter("state");

		Set<String> scopes = Collections.emptySet();
		String scopeParam = request.getParameter("scope");
		if (scopeParam != null && !scopeParam.trim().isEmpty()) {
			scopes = new HashSet<>(Arrays.asList(scopeParam.split(" ")));
		}

		Map<String, Object> additionalParameters = new HashMap<>();
		additionalParameters.put("auth_type", request.getParameter("auth_type"));
		additionalParameters.put("username", request.getParameter("username"));
		additionalParameters.put("credentials", request.getParameter("credentials"));

		Authentication clientPrincipal = createClientPrincipal(clientId);

		return new OAuth2AuthorizationCodeRequestAuthenticationToken(
				request.getRequestURI(),
				clientId,
				clientPrincipal,
				redirectUri,
				state,
				scopes,
				additionalParameters);
	}

	private Authentication createClientPrincipal(String clientId) {
		return new AnonymousAuthenticationToken(
				"oauth2-client-key",
				clientId,
				List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
	}
}
