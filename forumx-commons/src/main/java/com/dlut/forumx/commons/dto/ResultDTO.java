package com.dlut.forumx.commons.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class ResultDTO<T> {
	private Integer code;
	private String message;
	private T data;
	private String path;
	private String timestamp;

	public static <T> ResultDTO<T> success(T data) {
		ResultDTO<T> result = new ResultDTO<>();
		result.setCode(200);
		result.setMessage("success");
		result.setData(data);
		result.setTimestamp(Instant.now().toString());
		return result;
	}

	public static <T> ResultDTO<T> error(Integer code, String message) {
		ResultDTO<T> result = new ResultDTO<>();
		result.setCode(code);
		result.setMessage(message);
		result.setTimestamp(Instant.now().toString());
		return result;
	}

	public static <T> ResultDTO<T> error(Integer code, String message, String path) {
		ResultDTO<T> result = new ResultDTO<>();
		result.setCode(code);
		result.setMessage(message);
		result.setPath(path);
		result.setTimestamp(Instant.now().toString());
		return result;
	}
}
