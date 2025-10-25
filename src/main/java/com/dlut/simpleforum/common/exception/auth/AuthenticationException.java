package com.dlut.simpleforum.common.exception.auth;

import com.dlut.simpleforum.utils.MessageSourceUtils;

/**
 * @author Ishi_O
 * @since
 */
public class AuthenticationException extends RuntimeException {
	public AuthenticationException(String msg) {
		super(MessageSourceUtils.getMessage("error.auth", null) + msg);
	}
}
