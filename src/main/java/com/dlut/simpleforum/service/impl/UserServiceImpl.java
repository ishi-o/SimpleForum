package com.dlut.simpleforum.service.impl;

import org.springframework.stereotype.Service;

import com.dlut.simpleforum.entity.User;
import com.dlut.simpleforum.entity.User.UserStatus;
import com.dlut.simpleforum.repository.UserRepository;
import com.dlut.simpleforum.service.UserService;
import com.dlut.simpleforum.util.MessageSourceUtils;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void register(String username, String password) {
		userRepository.save(new User(username, password));
	}

	@Override
	public User login(String username, String password) {
		User user = userRepository.findByName(username)
				.orElseThrow(() -> new IllegalArgumentException(
						MessageSourceUtils.getMessage("error.user.not-found", null)));
		if (user.getStatus().equals(UserStatus.BLOCKED)) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.login.is-blocked", null));
		} else if (user.getStatus().equals(UserStatus.ACTIVE)) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.login.is-active", null));
		} else if (!user.getPassword().equals(password)) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.login.wrong-pwd", null));
		} else {
			user.setStatus(UserStatus.ACTIVE);
			return user;
		}
	}

	@Override
	public void logout(Long uid) {
		User user = userRepository.findById(uid)
				.orElseThrow(() -> new IllegalArgumentException(
						MessageSourceUtils.getMessage("error.user.not-found", null)));
		user.setStatus(UserStatus.OFFLINE);
	}
}
