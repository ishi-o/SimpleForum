package com.dlut.forumx.user.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.forumx.common.api.ApiResponse;
import com.dlut.forumx.common.api.ValidationGroup;
import com.dlut.forumx.user.dto.request.LoginRequest;
import com.dlut.forumx.user.dto.request.RegisterRequest;
import com.dlut.forumx.user.dto.request.VerificationCodeRequest;
import com.dlut.forumx.user.dto.request.VerificationSendRequest;
import com.dlut.forumx.user.dto.response.LoginResponse;
import com.dlut.forumx.user.model.security.UserPrincipal;
import com.dlut.forumx.user.verification.service.VerificationService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// TODO: auth requests
@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
	@Qualifier("smsVerificationService")
	private final VerificationService smsVerificationService;

	@Qualifier("emailVerificationService")
	private final VerificationService emailVerificationService;

	private final UserDetailsService userDetailsService;

	private final AuthenticationManager authenticationManager;

	@PostMapping("/login/password")
	public ApiResponse<LoginResponse> passwordLogin(@RequestBody LoginRequest req, HttpSession session) {
		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
		if (existingAuth != null && existingAuth.isAuthenticated()
				&& !(existingAuth instanceof AnonymousAuthenticationToken)) {
			throw new IllegalArgumentException("");
		}
		UsernamePasswordAuthenticationToken authReq = UsernamePasswordAuthenticationToken.unauthenticated(
				req.getAccount(),
				req.getCredential());
		Authentication auth = authenticationManager.authenticate(authReq);
		saveAuthContext(auth, session);
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		LoginResponse resp = LoginResponse.loginResponse(((UserPrincipal) userDetails).getUserId(),
				userDetails.getUsername(), session.getId());
		return ApiResponse.success(resp);
	}

	@PostMapping("/login/sms")
	public ApiResponse<LoginResponse> smsLogin(@RequestBody LoginRequest req, HttpSession session) {
		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
		if (existingAuth != null && existingAuth.isAuthenticated()
				&& !(existingAuth instanceof AnonymousAuthenticationToken)) {
			throw new IllegalArgumentException("");
		}
		if (!smsVerificationService.verifyCode(req.getAccount(), req.getCredential())) {
			throw new IllegalArgumentException("验证码错误");
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(req.getAccount());
		Authentication auth = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		saveAuthContext(auth, session);
		LoginResponse resp = LoginResponse.loginResponse(((UserPrincipal) userDetails).getUserId(),
				userDetails.getUsername(), session.getId());
		return ApiResponse.success(resp);
	}

	@PostMapping("/login/email")
	public ApiResponse<LoginResponse> emailLogin(@RequestBody LoginRequest req, HttpSession session) {
		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
		if (existingAuth != null && existingAuth.isAuthenticated()
				&& !(existingAuth instanceof AnonymousAuthenticationToken)) {
			throw new IllegalArgumentException("");
		}
		if (!emailVerificationService.verifyCode(req.getAccount(), req.getCredential())) {
			throw new IllegalArgumentException("验证码错误");
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(req.getAccount());
		Authentication auth = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		saveAuthContext(auth, session);
		LoginResponse resp = LoginResponse.loginResponse(((UserPrincipal) userDetails).getUserId(),
				userDetails.getUsername(), session.getId());
		return ApiResponse.success(resp);
	}

	// private void check

	private void saveAuthContext(Authentication auth, HttpSession session) {
		SecurityContext ctx = SecurityContextHolder.createEmptyContext();
		ctx.setAuthentication(auth);
		SecurityContextHolder.setContext(ctx);
		session.setAttribute("SPRING_SECURITY_CONTEXT", ctx);
	}

	@PostMapping("/register")
	public ApiResponse<Void> register(@RequestBody RegisterRequest req) {
		return ApiResponse.success();
	}

	@PostMapping("/sms")
	public ApiResponse<Void> sendSMS(
			@Validated(ValidationGroup.PhoneGroup.class) @RequestBody VerificationSendRequest req) {
		log.info("[Verification] Begin send SMS.");
		smsVerificationService.sendCode(req.getTarget());
		return ApiResponse.success();
	}

	@PostMapping("/sms/verification")
	public ApiResponse<Void> verifySms(
			@Validated(ValidationGroup.PhoneGroup.class) @RequestBody VerificationCodeRequest req) {
		log.info("[Verification] Begin verify SMS.");
		smsVerificationService.verifyCode(req.getTarget(), req.getCode());
		return ApiResponse.success();
	}

	@PostMapping("/email")
	public ApiResponse<Void> sendEmail(
			@Validated(ValidationGroup.EmailGroup.class) @RequestBody VerificationSendRequest req) {
		log.info("[Verification Email] Begin send Email.");
		emailVerificationService.sendCode(req.getTarget());
		return ApiResponse.success();
	}

	@PostMapping("/email/verification")
	public ApiResponse<Void> verifyEmail(
			@Validated(ValidationGroup.EmailGroup.class) @RequestBody VerificationCodeRequest req) {
		log.info("[Verification Email] Begin verify Email.");
		emailVerificationService.verifyCode(req.getTarget(), req.getCode());
		return ApiResponse.success();
	}
}
