package com.dlut.forumx.user.verification.service;

public interface VerificationService {

	void sendCode(String target);

	boolean verifyCode(String target, String code);
}
