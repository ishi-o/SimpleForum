package com.dlut.simpleforum.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson3JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import tools.jackson.databind.ObjectMapper;

/**
 * @author Ishi_O
 * @since
 */
@Configuration
public class RedisConfig {
	private final ObjectMapper objectMapper;

	public RedisConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		return RedisCacheManager.builder(redisConnectionFactory)
				.cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
						.entryTtl(Duration.ofMinutes(30))
						.serializeKeysWith(RedisSerializationContext.SerializationPair
								.fromSerializer(StringRedisSerializer.UTF_8))
						.serializeValuesWith(RedisSerializationContext.SerializationPair
								.fromSerializer(new GenericJackson3JsonRedisSerializer(objectMapper))))
				.build();
	}
}
