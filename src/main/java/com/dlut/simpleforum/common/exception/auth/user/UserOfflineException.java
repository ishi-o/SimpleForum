package com.dlut.simpleforum.common.exception.auth.user;

import com.dlut.simpleforum.common.exception.auth.AuthenticationException;
import com.dlut.simpleforum.utils.MessageSourceUtils;

/**
 * @author Ishi_O
 * @since
 */
public class UserOfflineException extends AuthenticationException {
	public UserOfflineException() {
		super(MessageSourceUtils.getMessage("error.auth.user.offline", null));
	}
}
