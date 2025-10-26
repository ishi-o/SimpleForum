package com.dlut.simpleforum.common.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson3JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Ishi_O
 * @since
 */
@Configuration
public class RedisConfig {
	@Bean
	public RedisTemplate<String, Object> redisTemplate(
			RedisConnectionFactory redisConnectionFactory,
			StringRedisSerializer stringRedisSerializer,
			GenericJackson3JsonRedisSerializer genericJackson3JsonRedisSerializer) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(genericJackson3JsonRedisSerializer);
		redisTemplate.setHashValueSerializer(genericJackson3JsonRedisSerializer);
		redisTemplate.setDefaultSerializer(genericJackson3JsonRedisSerializer);
		return redisTemplate;
	}

	@Bean
	public RedisCacheManager cacheManager(
			RedisConnectionFactory redisConnectionFactory,
			StringRedisSerializer stringRedisSerializer,
			GenericJackson3JsonRedisSerializer genericJackson3JsonRedisSerializer) {
		return RedisCacheManager.builder(redisConnectionFactory)
				.cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
						.entryTtl(Duration.ofMinutes(30))
						.serializeKeysWith(RedisSerializationContext.SerializationPair
								.fromSerializer(stringRedisSerializer))
						.serializeValuesWith(RedisSerializationContext.SerializationPair
								.fromSerializer(genericJackson3JsonRedisSerializer)))
				.build();
	}
}
