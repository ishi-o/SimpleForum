package com.dlut.forumx.content.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ObjectMapperUtils {
	private static ObjectMapper OBJECT_MAPPER;

	private ObjectMapperUtils(ObjectMapper objectMapper) {
		OBJECT_MAPPER = objectMapper;
	}

	public static String writeValueAsString(Object obj) throws JsonProcessingException {
		return OBJECT_MAPPER.writeValueAsString(obj);
	}
}
