package com.dlut.forumx.user.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.dlut.forumx.user.security.token.SmsAuthenticationToken;
import com.dlut.forumx.user.verification.service.SmsVerificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {

	private final UserDetailsService userDetailsService;
	private final SmsVerificationService smsVerificationService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SmsAuthenticationToken token = (SmsAuthenticationToken) authentication;
		String username = token.getPrincipal().toString();
		String password = token.getCredentials().toString();
		if (!smsVerificationService.verifyCode(username, password)) {
			throw new BadCredentialsException("");
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new SmsAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SmsAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
