package com.dlut.simpleforum.common.session;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dlut.simpleforum.entity.User;

import lombok.AllArgsConstructor;

/**
 * @author Ishi_O
 * @since
 */
@AllArgsConstructor
@Service
public class SessionManager {
	private static String SESSION_PREFIX = "session:";

	private final RedisTemplate<String, Object> redisTemplate;

	public void addActiveUser(User user) {
		redisTemplate.opsForValue().set(SESSION_PREFIX + user.getUid(), user);
	}

	public User getActiveUser(Long uid) {
		return (User) redisTemplate.opsForValue().get(SESSION_PREFIX + uid);
	}

	public void removeActiveUser(Long uid) {
		if (uid != null) {
			redisTemplate.delete(SESSION_PREFIX + uid);
		}
	}

	public boolean isUserLoggedIn(Long uid) {
		return redisTemplate.opsForValue().get(SESSION_PREFIX + uid) == null;
	}

	public static SessionUser getSessionUser() {
		return (SessionUser) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest()
				.getAttribute("sessionUser");
	}
}
