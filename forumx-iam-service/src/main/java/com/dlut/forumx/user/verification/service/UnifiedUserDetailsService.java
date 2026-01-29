package com.dlut.forumx.user.verification.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dlut.forumx.user.mapper.UserMapper;
import com.dlut.forumx.user.model.entity.Role;
import com.dlut.forumx.user.model.entity.User;
import com.dlut.forumx.user.model.security.UserPrincipal;
import com.dlut.forumx.user.verification.mq.VerificationMessage.VerificationType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnifiedUserDetailsService {

	private final UserMapper userMapper;

	public UserDetails loadUserByIdentifier(String id, VerificationType type) throws UsernameNotFoundException {
		User user = switch (type) {
			case PASSWORD -> userMapper.selectByUsername(id);
			case EMAIL -> userMapper.selectByEmail(id);
			case SMS -> userMapper.selectByPhone(id);
		};
		Long userId = user.getUserId();
		String password = user.getPassword();
		List<Role> roles = userMapper.selectAuthoritiesById(userId);
		return UserPrincipal.createUserPrincipal(userId, id, password,
				roles.stream()
						.map(r -> new SimpleGrantedAuthority(r.getRoleCode()))
						.toList());
	}
}
