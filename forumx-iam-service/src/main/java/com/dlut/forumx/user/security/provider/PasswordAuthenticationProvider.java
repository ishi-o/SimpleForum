package com.dlut.forumx.user.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dlut.forumx.user.security.token.PasswordAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordAuthenticationProvider implements AuthenticationProvider {

	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		PasswordAuthenticationToken token = (PasswordAuthenticationToken) authentication;
		String username = (String) token.getPrincipal();
		String password = (String) token.getCredentials();
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
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
