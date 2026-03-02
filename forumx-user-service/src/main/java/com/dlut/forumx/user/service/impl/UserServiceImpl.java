package com.dlut.forumx.user.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlut.forumx.user.entity.User;
import com.dlut.forumx.user.entity.UserStats;
import com.dlut.forumx.user.mapper.UserMapper;
import com.dlut.forumx.user.mapper.UserStatsMapper;
import com.dlut.forumx.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserStatsMapper userStatsMapper;
	private final StringRedisTemplate redisTemplate;
	private final ObjectMapper objectMapper;

	private static final String USER_CACHE_KEY = "user:info:";
	private static final String USER_STATS_CACHE_KEY = "user:stats:";
	private static final long CACHE_TTL_HOURS = 1;

	@Override
	public User getUserById(Long userId) {
		String cacheKey = USER_CACHE_KEY + userId;

		// 1. 先从Redis获取
		String cachedUser = redisTemplate.opsForValue().get(cacheKey);
		if (cachedUser != null) {
			try {
				return objectMapper.readValue(cachedUser, User.class);
			} catch (JsonProcessingException e) {
				log.error("反序列化用户缓存失败: userId={}", userId, e);
				redisTemplate.delete(cacheKey);
			}
		}

		// 2. 缓存未命中，查数据库
		User user = userMapper.selectById(userId);

		// 3. 写入缓存
		if (user != null) {
			try {
				String userJson = objectMapper.writeValueAsString(user);
				redisTemplate.opsForValue().set(cacheKey, userJson, CACHE_TTL_HOURS, TimeUnit.HOURS);
			} catch (JsonProcessingException e) {
				log.error("序列化用户缓存失败: userId={}", userId, e);
			}
		}

		return user;
	}

	@Override
	public User getUserByKeycloakId(String keycloakId) {
		// KeycloakID不常用，不缓存
		return userMapper.selectByKeycloakId(keycloakId);
	}

	@Override
	public List<User> getUsersByIds(List<Long> userIds) {
		if (userIds == null || userIds.isEmpty()) {
			return List.of();
		}

		// 批量查询不缓存，直接查DB
		return userMapper.selectByIds(userIds);
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		userMapper.updateById(user);

		// 删除缓存
		String cacheKey = USER_CACHE_KEY + user.getId();
		redisTemplate.delete(cacheKey);
	}

	@Override
	public Long getUserIdByKeycloakId(String keycloakId) {
		User user = userMapper.selectByKeycloakId(keycloakId);
		return user != null ? user.getId() : null;
	}

	@Override
	public UserStats getUserStats(Long userId) {
		String cacheKey = USER_STATS_CACHE_KEY + userId;

		// 1. 先从Redis获取
		String cachedStats = redisTemplate.opsForValue().get(cacheKey);
		if (cachedStats != null) {
			try {
				return objectMapper.readValue(cachedStats, UserStats.class);
			} catch (JsonProcessingException e) {
				log.error("反序列化统计缓存失败: userId={}", userId, e);
				redisTemplate.delete(cacheKey);
			}
		}

		// 2. 缓存未命中，查数据库
		UserStats stats = userStatsMapper.selectByUserId(userId);

		// 3. 如果不存在则初始化
		if (stats == null) {
			userStatsMapper.initStats(userId);
			stats = userStatsMapper.selectByUserId(userId);
		}

		// 4. 写入缓存
		if (stats != null) {
			try {
				String statsJson = objectMapper.writeValueAsString(stats);
				redisTemplate.opsForValue().set(cacheKey, statsJson, CACHE_TTL_HOURS, TimeUnit.HOURS);
			} catch (JsonProcessingException e) {
				log.error("序列化统计缓存失败: userId={}", userId, e);
			}
		}

		return stats;
	}

	@Override
	@Transactional
	public User createUser(User user) {
		userMapper.insert(user);
		userStatsMapper.initStats(user.getId());
		return user;
	}
}
