package com.dlut.simpleforum.common.session;

import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
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
		return redisTemplate.opsForValue().get(SESSION_PREFIX + uid) != null;
	}

	@EventListener
	@Profile("dev")
	public void cleanSessionUser(ContextClosedEvent cce) {
		String pattern = SESSION_PREFIX + "*";
		Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions()
				.match(pattern)
				.count(1000)
				.build());
		while (cursor.hasNext()) {
			String key = cursor.next();
			redisTemplate.delete(key);
		}
		cursor.close();
	}

	public static SessionUser getSessionUser() {
		return (SessionUser) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest()
				.getSession()
				.getAttribute("sessionUser");
	}
}
