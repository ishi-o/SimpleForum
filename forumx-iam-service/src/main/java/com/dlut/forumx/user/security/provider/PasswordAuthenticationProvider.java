package com.dlut.forumx.user.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dlut.forumx.user.security.token.PasswordAuthenticationToken;
import com.dlut.forumx.user.verification.mq.VerificationMessage.VerificationType;
import com.dlut.forumx.user.verification.service.UnifiedUserDetailsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordAuthenticationProvider implements AuthenticationProvider {

	private final UnifiedUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		PasswordAuthenticationToken token = (PasswordAuthenticationToken) authentication;
		String username = token.getPrincipal().toString();
		String password = token.getCredentials().toString();
		UserDetails userDetails = userDetailsService.loadUserByIdentifier(username, VerificationType.PASSWORD);
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("");
		}
		return new PasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
