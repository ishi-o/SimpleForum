package com.dlut.simpleforum.common.exception.auth.user;

import com.dlut.simpleforum.common.exception.auth.AuthenticationException;
import com.dlut.simpleforum.utils.MessageSourceUtils;

/**
 * @author Ishi_O
 * @since
 */
public class UserLoggedInException extends AuthenticationException {
	public UserLoggedInException() {
		super(MessageSourceUtils.getMessage("error.auth.user.logged-in", null));
	}
}
