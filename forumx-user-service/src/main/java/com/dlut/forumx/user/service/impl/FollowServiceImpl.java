package com.dlut.forumx.user.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlut.forumx.user.entity.UserFollow;
import com.dlut.forumx.user.mapper.UserFollowMapper;
import com.dlut.forumx.user.mapper.UserStatsMapper;
import com.dlut.forumx.user.service.FollowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

	private final UserFollowMapper followMapper;
	private final UserStatsMapper statsMapper;
	private final StringRedisTemplate redisTemplate;

	private static final String FOLLOW_SET_KEY = "follow:set:";
	private static final String FAN_SET_KEY = "fan:set:";
	private static final String FOLLOW_COUNT_KEY = "follow:count:";
	private static final String FAN_COUNT_KEY = "fan:count:";

	private static final long COUNT_CACHE_TTL_HOURS = 1;
	private static final long SET_CACHE_TTL_HOURS = 24;

	@Override
	@Transactional
	public void follow(Long followerId, Long followedId) {
		if (followerId.equals(followedId)) {
			throw new IllegalArgumentException("不能关注自己");
		}

		UserFollow existing = followMapper.select(followerId, followedId);
		if (existing != null) {
			return;
		}

		UserFollow follow = new UserFollow();
		follow.setFollowerId(followerId);
		follow.setFollowedId(followedId);
		follow.setFollowTime(LocalDateTime.now());
		followMapper.insert(follow);

		statsMapper.incrFollowCount(followerId, 1);
		statsMapper.incrFansCount(followedId, 1);

		// 更新缓存
		updateFollowCache(followerId, followedId, true);
	}

	@Override
	@Transactional
	public void unfollow(Long followerId, Long followedId) {
		followMapper.delete(followerId, followedId);

		statsMapper.incrFollowCount(followerId, -1);
		statsMapper.incrFansCount(followedId, -1);

		// 更新缓存
		updateFollowCache(followerId, followedId, false);
	}

	@Override
	public boolean isFollowing(Long followerId, Long followedId) {
		String key = FOLLOW_SET_KEY + followerId;

		// 1. 查Redis Set
		Boolean isMember = redisTemplate.opsForSet().isMember(key, String.valueOf(followedId));
		if (Boolean.TRUE.equals(isMember)) {
			return true;
		}

		// 2. Redis没有，查数据库
		UserFollow follow = followMapper.select(followerId, followedId);

		// 3. 如果关注了，更新Redis Set
		if (follow != null) {
			redisTemplate.opsForSet().add(key, String.valueOf(followedId));
			redisTemplate.expire(key, SET_CACHE_TTL_HOURS, TimeUnit.HOURS);
		}

		return follow != null;
	}

	@Override
	public Map<Long, Boolean> batchCheckFollowing(Long followerId, List<Long> targetIds) {
		Map<Long, Boolean> result = new HashMap<>();
		if (targetIds == null || targetIds.isEmpty()) {
			return result;
		}

		String key = FOLLOW_SET_KEY + followerId;
		List<Long> missingIds = new ArrayList<>();

		// 1. 逐个查询Redis（Redis不支持批量isMember）
		for (Long targetId : targetIds) {
			Boolean isMember = redisTemplate.opsForSet().isMember(key, String.valueOf(targetId));
			if (isMember != null) {
				result.put(targetId, isMember);
			} else {
				missingIds.add(targetId);
			}
		}

		// 2. 查数据库补全缺失的
		if (!missingIds.isEmpty()) {
			List<UserFollow> relations = followMapper.selectFollowRelations(followerId, missingIds);
			Set<Long> followingSet = relations.stream()
					.map(UserFollow::getFollowedId)
					.collect(Collectors.toSet());

			for (Long missingId : missingIds) {
				boolean isFollowing = followingSet.contains(missingId);
				result.put(missingId, isFollowing);

				// 3. 更新Redis
				if (isFollowing) {
					redisTemplate.opsForSet().add(key, String.valueOf(missingId));
				}
			}

			redisTemplate.expire(key, SET_CACHE_TTL_HOURS, TimeUnit.HOURS);
		}

		return result;
	}

	@Override
	public PageInfo<UserFollow> getFollowers(Long userId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<UserFollow> follows = followMapper.selectByFollowedId(userId);
		return new PageInfo<>(follows);
	}

	@Override
	public PageInfo<UserFollow> getFollowing(Long userId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<UserFollow> follows = followMapper.selectByFollowerId(userId);
		return new PageInfo<>(follows);
	}

	@Override
	public int getFollowerCount(Long userId) {
		String cacheKey = FAN_COUNT_KEY + userId;

		// 1. 查Redis
		String cachedCount = redisTemplate.opsForValue().get(cacheKey);
		if (cachedCount != null) {
			return Integer.parseInt(cachedCount);
		}

		// 2. 查数据库
		int dbCount = followMapper.countFollowers(userId);

		// 3. 写入缓存
		redisTemplate.opsForValue().set(cacheKey, String.valueOf(dbCount),
				COUNT_CACHE_TTL_HOURS, TimeUnit.HOURS);
		return dbCount;
	}

	@Override
	public int getFollowingCount(Long userId) {
		String cacheKey = FOLLOW_COUNT_KEY + userId;

		// 1. 查Redis
		String cachedCount = redisTemplate.opsForValue().get(cacheKey);
		if (cachedCount != null) {
			return Integer.parseInt(cachedCount);
		}

		// 2. 查数据库
		int dbCount = followMapper.countFollowing(userId);

		// 3. 写入缓存
		redisTemplate.opsForValue().set(cacheKey, String.valueOf(dbCount),
				COUNT_CACHE_TTL_HOURS, TimeUnit.HOURS);
		return dbCount;
	}

	@Override
	public List<Long> getFollowerIds(Long userId) {
		String key = FAN_SET_KEY + userId;

		// 1. 查Redis Set
		Set<String> cachedIds = redisTemplate.opsForSet().members(key);
		if (cachedIds != null && !cachedIds.isEmpty()) {
			return cachedIds.stream()
					.map(Long::parseLong)
					.collect(Collectors.toList());
		}

		// 2. 查数据库
		List<Long> ids = followMapper.selectFollowers(userId, 0, Integer.MAX_VALUE);

		// 3. 写入Redis
		if (!ids.isEmpty()) {
			String[] idArray = ids.stream()
					.map(String::valueOf)
					.toArray(String[]::new);
			redisTemplate.opsForSet().add(key, idArray);
			redisTemplate.expire(key, SET_CACHE_TTL_HOURS, TimeUnit.HOURS);
		}

		return ids;
	}

	@Override
	public List<Long> getFollowingIds(Long userId) {
		String key = FOLLOW_SET_KEY + userId;

		// 1. 查Redis Set
		Set<String> cachedIds = redisTemplate.opsForSet().members(key);
		if (cachedIds != null && !cachedIds.isEmpty()) {
			return cachedIds.stream()
					.map(Long::parseLong)
					.collect(Collectors.toList());
		}

		// 2. 查数据库
		List<Long> ids = followMapper.selectFollowing(userId, 0, Integer.MAX_VALUE);

		// 3. 写入Redis
		if (!ids.isEmpty()) {
			String[] idArray = ids.stream()
					.map(String::valueOf)
					.toArray(String[]::new);
			redisTemplate.opsForSet().add(key, idArray);
			redisTemplate.expire(key, SET_CACHE_TTL_HOURS, TimeUnit.HOURS);
		}

		return ids;
	}

	private void updateFollowCache(Long followerId, Long followedId, boolean isFollow) {
		String followKey = FOLLOW_SET_KEY + followerId;
		String fanKey = FAN_SET_KEY + followedId;

		if (isFollow) {
			// 添加关注关系
			redisTemplate.opsForSet().add(followKey, String.valueOf(followedId));
			redisTemplate.opsForSet().add(fanKey, String.valueOf(followerId));
			redisTemplate.expire(followKey, SET_CACHE_TTL_HOURS, TimeUnit.HOURS);
			redisTemplate.expire(fanKey, SET_CACHE_TTL_HOURS, TimeUnit.HOURS);
		} else {
			// 移除关注关系
			redisTemplate.opsForSet().remove(followKey, String.valueOf(followedId));
			redisTemplate.opsForSet().remove(fanKey, String.valueOf(followerId));
		}

		// 删除计数缓存
		redisTemplate.delete(FOLLOW_COUNT_KEY + followerId);
		redisTemplate.delete(FAN_COUNT_KEY + followedId);
	}
}
