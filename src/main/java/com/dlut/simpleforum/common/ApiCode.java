package com.dlut.simpleforum.common;

import lombok.Getter;

@Getter
public enum ApiCode {

	// 2000
	OK(2000, "OK"),
	// 4000
	FAILURE(4000, "FAILURE");

	private static final String MESSAGE_PREFIX = "api.response.";

	private final Integer apiCode;
	private final String code;

	ApiCode(Integer apiCode, String code) {
		this.apiCode = apiCode;
		this.code = code;
	}

	public boolean isSuccess() {
		return apiCode >= 2000 && apiCode < 3000;
	}

	public String getMessageCode() {
		return MESSAGE_PREFIX + code.toLowerCase();
	}

}