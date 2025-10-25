package com.dlut.simpleforum.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson3JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

/**
 * @author Ishi_O
 * @since
 */
@Configuration
public class SerializerConfig {
	@Bean
	public StringRedisSerializer stringSerializer() {
		return StringRedisSerializer.UTF_8;
	}

	@Bean
	public ObjectMapper objectMapper() {
		return JsonMapper.builder()
				.activateDefaultTyping(
						BasicPolymorphicTypeValidator.builder()
								.allowIfSubType("java.util.")
								.allowIfSubType("org.springframework.data.domain.")
								.allowIfSubType("com.dlut.simpleforum.")
								.build(),
						DefaultTyping.NON_FINAL,
						JsonTypeInfo.As.PROPERTY)
				.build();
	}

	@Bean
	public GenericJackson3JsonRedisSerializer genericJackson3JsonRedisSerializer(ObjectMapper objectMapper) {
		return new GenericJackson3JsonRedisSerializer(objectMapper);
	}

	@Bean
	public HessianRedisSerializer<Object> hessianRedisSerializer() {
		return new HessianRedisSerializer<>();
	}
}
