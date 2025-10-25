package com.dlut.simpleforum.service;

import com.dlut.simpleforum.common.session.SessionUser;
import com.dlut.simpleforum.entity.User;

/**
 * @author Ishi_O
 * @since
 */
public interface AuthenticationService {
	User login(SessionUser sessionUser, String username, String password);

	void logout(SessionUser sessionUser);

	User getMe(SessionUser sessionUser);
}
