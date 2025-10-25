package com.dlut.simpleforum.common.exception.auth.user;

import com.dlut.simpleforum.common.exception.auth.AuthenticationException;
import com.dlut.simpleforum.utils.MessageSourceUtils;

/**
 * @author Ishi_O
 * @since
 */
public class UserNotFoundException extends AuthenticationException {
	public UserNotFoundException() {
		super(MessageSourceUtils.getMessage("error.auth.uer.not-found", null));
	}
}
