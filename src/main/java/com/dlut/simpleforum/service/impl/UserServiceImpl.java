package com.dlut.simpleforum.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlut.simpleforum.common.exception.auth.user.UserNotFoundException;
import com.dlut.simpleforum.entity.User;
import com.dlut.simpleforum.repository.UserRepository;
import com.dlut.simpleforum.service.UserService;

import lombok.AllArgsConstructor;

/**
 * @author Ishi_O
 * @since
 */
@AllArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User getUser(Long uid) {
		return userRepository.findById(uid).orElseThrow(() -> new UserNotFoundException());
	}

	@Override
	public void register(String username, String password) {
		User user = new User(username, passwordEncoder.encode(password));
		userRepository.save(user);
	}
}
