package com.dlut.forumx.user.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.dlut.forumx.user.security.token.SmsAuthenticationToken;
import com.dlut.forumx.user.verification.mq.VerificationMessage.VerificationType;
import com.dlut.forumx.user.verification.service.SmsVerificationService;
import com.dlut.forumx.user.verification.service.UnifiedUserDetailsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsAuthenticationProvider implements AuthenticationProvider {

	private final UnifiedUserDetailsService userDetailsService;
	private final SmsVerificationService smsVerificationService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SmsAuthenticationToken token = (SmsAuthenticationToken) authentication;
		String phone = token.getPrincipal().toString();
		String password = token.getCredentials().toString();
		if (!smsVerificationService.verifyCode(phone, password)) {
			throw new BadCredentialsException("");
		}
		UserDetails userDetails = userDetailsService.loadUserByIdentifier(phone, VerificationType.SMS);
		return new SmsAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SmsAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
