package com.dlut.forumx.user.verification.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dlut.forumx.user.mapper.UserMapper;
import com.dlut.forumx.user.model.entity.User;
import com.dlut.forumx.user.model.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnifiedUserDetailsService implements UserDetailsService {

	private final UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.selectByUsername(username);
		Long userId = user.getUserId();
		String password = user.getPassword();
		return UserPrincipal.createUserPrincipal(userId, username, password, List.of());
	}

}
