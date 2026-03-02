package com.dlut.forumx.user.service.impl;

import com.dlut.forumx.user.entity.User;
import com.dlut.forumx.user.entity.UserStats;
import com.dlut.forumx.user.mapper.UserMapper;
import com.dlut.forumx.user.mapper.UserStatsMapper;
import com.dlut.forumx.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserStatsMapper userStatsMapper;

	@Override
	public User getUserById(Long userId) {
		return userMapper.selectById(userId);
	}

	@Override
	public User getUserByKeycloakId(String keycloakId) {
		return userMapper.selectByKeycloakId(keycloakId);
	}

	@Override
	public List<User> getUsersByIds(List<Long> userIds) {
		if (userIds == null || userIds.isEmpty()) {
			return List.of();
		}
		return userMapper.selectByIds(userIds);
	}

	@Override
	@Transactional
	public void createUser(User user) {
		// 1. 插入用户
		userMapper.insert(user);

		// 2. 初始化统计
		UserStats stats = new UserStats();
		stats.setUserId(user.getId());
		userStatsMapper.insert(stats);

		log.info("创建用户成功: userId={}, keycloakId={}", user.getId(), user.getKeycloakId());
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		userMapper.updateById(user);
		log.info("更新用户成功: userId={}", user.getId());
	}

	@Override
	public Long getUserIdByKeycloakId(String keycloakId) {
		User user = userMapper.selectByKeycloakId(keycloakId);
		return user != null ? user.getId() : null;
	}

	@Override
	public UserStats getUserStats(Long userId) {
		return userStatsMapper.selectByUserId(userId);
	}
}
