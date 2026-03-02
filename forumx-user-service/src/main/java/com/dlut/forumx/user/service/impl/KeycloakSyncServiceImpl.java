package com.dlut.forumx.user.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlut.forumx.user.entity.User;
import com.dlut.forumx.user.service.KeycloakSyncService;
import com.dlut.forumx.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakSyncServiceImpl implements KeycloakSyncService {

	private final UserService userService;

	@Override
	@Transactional
	public void handleUserCreated(String keycloakId, String username, String email) {
		log.info("同步Keycloak新用户: {}, {}, {}", keycloakId, username, email);

		// 检查用户是否已存在
		Long existingUserId = userService.getUserIdByKeycloakId(keycloakId);
		if (existingUserId != null) {
			log.warn("用户已存在: keycloakId={}, userId={}", keycloakId, existingUserId);
			return;
		}

		// 创建新用户（使用 User 实体）
		User user = new User();
		user.setKeycloakId(keycloakId);
		user.setUsername(username);
		user.setEmail(email);
		user.setNickname(username);
		user.setStatus(1);
		user.setRegisterTime(LocalDateTime.now());

		userService.createUser(user);
	}

	@Override
	@Transactional
	public void handleUserUpdated(String keycloakId, String username, String email) {
		log.info("同步Keycloak更新用户: {}, {}, {}", keycloakId, username, email);

		User user = userService.getUserByKeycloakId(keycloakId);
		if (user == null) {
			log.warn("用户不存在，尝试创建: keycloakId={}", keycloakId);
			handleUserCreated(keycloakId, username, email);
			return;
		}

		user.setUsername(username);
		user.setEmail(email);
		userService.updateUser(user);
	}

	@Override
	public void handleUserDeleted(String keycloakId) {
		log.info("同步Keycloak删除用户: {}", keycloakId);
		// 业务数据保留，不做处理
	}
}
