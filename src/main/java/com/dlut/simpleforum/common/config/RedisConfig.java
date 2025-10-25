package com.dlut.simpleforum.common.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
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
			HessianRedisSerializer<Object> hessianRedisSerializer) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(hessianRedisSerializer);
		redisTemplate.setHashValueSerializer(hessianRedisSerializer);
		return redisTemplate;
	}

	@Bean
	public RedisCacheManager cacheManager(
			RedisConnectionFactory redisConnectionFactory,
			StringRedisSerializer stringRedisSerializer,
			HessianRedisSerializer<Object> hessianRedisSerializer) {
		return RedisCacheManager.builder(redisConnectionFactory)
				.cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
						.entryTtl(Duration.ofMinutes(30))
						.serializeKeysWith(RedisSerializationContext.SerializationPair
								.fromSerializer(stringRedisSerializer))
						.serializeValuesWith(RedisSerializationContext.SerializationPair
								.fromSerializer(hessianRedisSerializer)))
				.build();
	}
}
