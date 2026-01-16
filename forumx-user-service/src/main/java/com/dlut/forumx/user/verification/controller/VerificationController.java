package com.dlut.forumx.user.verification.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.forumx.common.api.ApiResponse;
import com.dlut.forumx.common.api.ValidationGroup;
import com.dlut.forumx.user.dto.request.VerificationCodeRequest;
import com.dlut.forumx.user.dto.request.VerificationSendRequest;
import com.dlut.forumx.user.verification.service.VerificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/verification")
@Slf4j
@RequiredArgsConstructor
public class VerificationController {

	@Qualifier("smsVerificationService")
	private final VerificationService smsVerificationService;

	@Qualifier("emailVerificationService")
	private final VerificationService emailVerificationService;

	@PostMapping("/sms/send")
	public ApiResponse<Void> sendSMS(
			@Validated(ValidationGroup.PhoneGroup.class) @RequestBody VerificationSendRequest req) {
		log.info("[Verification] Begin send SMS.");
		smsVerificationService.sendCode(req.getTarget());
		return ApiResponse.success();
	}

	@PostMapping("/sms")
	public ApiResponse<Void> verifySms(
			@Validated(ValidationGroup.PhoneGroup.class) @RequestBody VerificationCodeRequest req) {
		log.info("[Verification] Begin verify SMS.");
		smsVerificationService.verifyCode(req.getTarget(), req.getCode());
		return ApiResponse.success();
	}

	@PostMapping("/email/send")
	public ApiResponse<Void> sendEmail(
			@Validated(ValidationGroup.EmailGroup.class) @RequestBody VerificationSendRequest req) {
		log.info("[Verification Email] Begin send Email.");
		emailVerificationService.sendCode(req.getTarget());
		return ApiResponse.success();
	}

	@PostMapping("/email")
	public ApiResponse<Void> verifyEmail(
			@Validated(ValidationGroup.EmailGroup.class) @RequestBody VerificationCodeRequest req) {
		log.info("[Verification Email] Begin verify Email.");
		emailVerificationService.verifyCode(req.getTarget(), req.getCode());
		return ApiResponse.success();
	}
}
