package com.dlut.simpleforum.service.impl;

import java.util.Optional;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Cacheable(cacheNames = "users", key = "#uid")
	@Override
	public User getUser(Long uid) {
		return userRepository.findById(uid)
				.orElseThrow(() -> new IllegalArgumentException(
						MessageSourceUtils.getMessage("error.user.not-found", null)));
	}

	@Override
	public void register(String username, String password) {
		Optional<User> userOpt = userRepository.findByName(username);
		if (userOpt.isPresent()) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.duplicate", null));
		} else {
			userRepository.save(new User(username, passwordEncoder.encode(password)));
		}
	}

	@CachePut(cacheNames = "users", key = "#result.uid")
	@Override
	public User login(String username, String password) {
		User user = userRepository.findByName(username)
				.orElseThrow(() -> new IllegalArgumentException(
						MessageSourceUtils.getMessage("error.user.not-found", null)));
		if (user.getStatus().equals(UserStatus.BLOCKED)) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.login.is-blocked", null));
		} else if (user.getStatus().equals(UserStatus.ACTIVE)) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.login.is-active", null));
		} else if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.login.wrong-pwd", null));
		} else {
			user.setStatus(UserStatus.ACTIVE);
			return user;
		}
	}

	@CachePut(cacheNames = "users", key = "#uid")
	@Override
	public User logout(Long uid) {
		User user = userRepository.findById(uid)
				.orElseThrow(() -> new IllegalArgumentException(
						MessageSourceUtils.getMessage("error.user.not-found", null)));
		user.setStatus(UserStatus.OFFLINE);
		return user;
	}
}
