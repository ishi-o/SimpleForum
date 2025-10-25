package com.dlut.simpleforum.common.exception.auth.user;

import com.dlut.simpleforum.common.exception.auth.AuthenticationException;
import com.dlut.simpleforum.utils.MessageSourceUtils;

/**
 * @author Ishi_O
 * @since
 */
public class WrongPasswordException extends AuthenticationException {
	public WrongPasswordException() {
		super(MessageSourceUtils.getMessage("error.auth.user.wrong-pwd", null));
	}
}
