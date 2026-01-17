package com.dlut.forumx.user.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.dlut.forumx.user.security.token.EmailAuthenticationToken;
import com.dlut.forumx.user.security.token.SmsAuthenticationToken;
import com.dlut.forumx.user.verification.service.EmailVerificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailAuthenticationProvider implements AuthenticationProvider {

	private final UserDetailsService userDetailsService;
	private final EmailVerificationService emailVerificationService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SmsAuthenticationToken token = (SmsAuthenticationToken) authentication;
		String username = token.getPrincipal().toString();
		String password = token.getCredentials().toString();
		if (!emailVerificationService.verifyCode(username, password)) {
			throw new BadCredentialsException("");
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new EmailAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SmsAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
