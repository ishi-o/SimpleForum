package com.dlut.simpleforum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
abstract class PageMixIn {
}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
abstract class SliceMixIn {
}

/**
 * @author Ishi_O
 * @since
 */
@Configuration
public class SerializerConfig {
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
}
