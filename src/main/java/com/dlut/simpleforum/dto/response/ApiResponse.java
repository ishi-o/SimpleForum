package com.dlut.simpleforum.dto.response;

import java.time.LocalDateTime;

import com.dlut.simpleforum.util.MessageSourceUtils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
	private Boolean success;
	private String code;
	private String message;
	private T data;
	private LocalDateTime timestamp;

	private static final String DEFAULT_SUCCESS_CODE = "SUCCESS";
	private static final String DEFAULT_FAILURE_CODE = "FAILURE";
	private static final String DEFAULT_SUCCESS_MESSAGE_CODE = "api.response.ok";
	private static final String DEFAULT_FAILURE_MESSAGE_CODE = "api.response.failure";

	public static <T> ApiResponse<T> success(Class<T> clazz) {
		return success(clazz, MessageSourceUtils.getMessage(DEFAULT_SUCCESS_MESSAGE_CODE, null));
	}

	public static <T> ApiResponse<T> success(Class<T> clazz, String message) {
		return of(clazz, DEFAULT_SUCCESS_CODE, message);
	}

	public static <T> ApiResponse<T> success(T data) {
		return of(data, DEFAULT_SUCCESS_CODE, MessageSourceUtils.getMessage(DEFAULT_SUCCESS_MESSAGE_CODE, null));
	}

	public static <T> ApiResponse<T> failure(Class<T> clazz) {
		return success(clazz, MessageSourceUtils.getMessage(DEFAULT_FAILURE_MESSAGE_CODE, null));
	}

	public static <T> ApiResponse<T> failure(Class<T> clazz, String message) {
		return of(clazz, DEFAULT_FAILURE_CODE, message);
	}

	public static <T> ApiResponse<T> failure(T data) {
		return of(data, DEFAULT_FAILURE_CODE, MessageSourceUtils.getMessage(DEFAULT_FAILURE_MESSAGE_CODE, null));
	}

	public static <T> ApiResponse<T> of(Class<T> clazz, String code, String message) {
		return ApiResponse.<T>builder()
				.code(code)
				.message(message)
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}

	public static <T> ApiResponse<T> of(T data, String code, String message) {
		return ApiResponse.<T>builder()
				.code(code)
				.message(message)
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();
	}
}
