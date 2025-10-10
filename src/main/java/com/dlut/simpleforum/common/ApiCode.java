package com.dlut.simpleforum.common;

import com.dlut.simpleforum.util.MessageSourceUtils;

import lombok.Getter;

@Getter
public enum ApiCode {

	// 2000
	OK(2000, "OK", MessageSourceUtils.getMessage(ApiCode.BASE_CODE + "ok", null)),
	// 4000
	FAILURE(4000, "FAILURE", MessageSourceUtils.getMessage(ApiCode.BASE_CODE + "failure", null));

	private static final String BASE_CODE = "api.response";

	private final Integer apiCode;
	private final String code;
	private final String message;

	ApiCode(Integer apiCode, String code, String message) {
		this.apiCode = apiCode;
		this.code = code;
		this.message = message;
	}

	public boolean isSuccess() {
		return apiCode >= 2000 && apiCode < 3000;
	}

}