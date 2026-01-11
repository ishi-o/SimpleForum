package com.dlut.simpleforum.util;

import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class ObjectMapperUtils {
	private static ObjectMapper OBJECT_MAPPER;

	private ObjectMapperUtils(ObjectMapper objectMapper) {
		OBJECT_MAPPER = objectMapper;
	}

	public static String writeValueAsString(Object obj) {
		return OBJECT_MAPPER.writeValueAsString(obj);
	}
}
