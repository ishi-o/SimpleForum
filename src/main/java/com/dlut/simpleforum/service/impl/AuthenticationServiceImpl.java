package com.dlut.simpleforum.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlut.simpleforum.common.annotation.RequireRole;
import com.dlut.simpleforum.common.enums.UserRole;
import com.dlut.simpleforum.common.exception.auth.user.UserLoggedInException;
import com.dlut.simpleforum.common.exception.auth.user.UserNotFoundException;
import com.dlut.simpleforum.common.exception.auth.user.WrongPasswordException;
import com.dlut.simpleforum.common.session.SessionManager;
import com.dlut.simpleforum.common.session.SessionUser;
import com.dlut.simpleforum.entity.User;
import com.dlut.simpleforum.repository.UserRepository;
import com.dlut.simpleforum.service.AuthenticationService;

import lombok.AllArgsConstructor;

/**
 * @author Ishi_O
 * @since
 */
@AllArgsConstructor
@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final SessionManager sessionManager;
	private final PasswordEncoder passwordEncoder;

	@RequireRole(exclude = UserRole.GUEST)
	@Transactional(readOnly = true)
	@Override
	public User getMe(SessionUser sessionUser) {
		return sessionManager.getActiveUser(sessionUser.getUid());
	}

	@RequireRole(include = UserRole.GUEST)
	@Transactional
	@Override
	public User login(SessionUser sessionUser, String username, String password) {
		User user = userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException());
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new WrongPasswordException();
		} else if (sessionManager.isUserLoggedIn(user.getUid())) {
			throw new UserLoggedInException();
		}
		sessionUser.userLogin(user);
		sessionManager.addActiveUser(user);
		return user;
	}

	@RequireRole(exclude = UserRole.GUEST)
	@Transactional
	@Override
	public void logout(SessionUser sessionUser) {
		sessionUser.userLogout();
		sessionManager.removeActiveUser(sessionUser.getUid());
	}

}
