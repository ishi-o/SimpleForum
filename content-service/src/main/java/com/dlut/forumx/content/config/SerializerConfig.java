package com.dlut.forumx.content.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
				.addModule(new JavaTimeModule())
				.activateDefaultTyping(
						BasicPolymorphicTypeValidator.builder()
								.allowIfSubType("java.util.")
								.allowIfSubType("org.springframework.data.domain.")
								.allowIfSubType("com.dlut.forumx.content.")
								.build(),
						DefaultTyping.NON_FINAL,
						JsonTypeInfo.As.PROPERTY)
				.build();
	}
}
