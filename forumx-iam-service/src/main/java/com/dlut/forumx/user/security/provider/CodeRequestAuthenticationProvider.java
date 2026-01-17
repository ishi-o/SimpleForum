package com.dlut.forumx.user.security.provider;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.stereotype.Component;

import com.dlut.forumx.user.security.token.EmailAuthenticationToken;
import com.dlut.forumx.user.security.token.PasswordAuthenticationToken;
import com.dlut.forumx.user.security.token.SmsAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CodeRequestAuthenticationProvider implements AuthenticationProvider {
	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!(authentication instanceof OAuth2AuthorizationCodeRequestAuthenticationToken authRequest)) {
			return null;
		}

		Map<String, Object> params = authRequest.getAdditionalParameters();
		String authType = (String) params.get("auth_type");
		String username = (String) params.get("username");
		String credentials = (String) params.get("credentials");

		if (username == null || credentials == null) {
			return authRequest;
		}

		Authentication userAuthRequest = createAuthenticationRequest(authType, username, credentials);

		Authentication authenticatedUser = authenticationManager.authenticate(userAuthRequest);

		return new OAuth2AuthorizationCodeRequestAuthenticationToken(
				authRequest.getAuthorizationUri(),
				authRequest.getClientId(),
				authenticatedUser,
				authRequest.getRedirectUri(),
				authRequest.getState(),
				authRequest.getScopes(),
				params);
	}

	private Authentication createAuthenticationRequest(String authType, String username, String credentials) {
		if ("password".equals(authType)) {
			return new PasswordAuthenticationToken(username, credentials);
		} else if ("otp:sms".equals(authType)) {
			return new SmsAuthenticationToken(username, credentials);
		} else if ("otp:email".equals(authType)) {
			return new EmailAuthenticationToken(username, credentials);
		} else {
			throw new BadCredentialsException("Unsupported auth type: " + authType);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2AuthorizationCodeRequestAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
