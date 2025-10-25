package com.dlut.simpleforum.common.constant;

import lombok.Getter;

@Getter
public enum ApiCode {

	// 2000
	OK(2000),
	// 4000
	FAILURE(4000);

	private static final String MESSAGE_PREFIX = "api.";

	private final Integer code;

	ApiCode(Integer code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return code >= 2000 && code < 3000;
	}

	public String getMessageCode() {
		return MESSAGE_PREFIX + code.toString();
	}

}