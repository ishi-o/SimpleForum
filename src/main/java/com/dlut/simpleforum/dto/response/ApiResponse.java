package com.dlut.simpleforum.dto.response;

import java.time.LocalDateTime;

import com.dlut.simpleforum.common.ApiCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
	private Boolean success;
	private ApiCode code;
	private T data;
	private LocalDateTime timestamp;

	public static <T> ApiResponse<T> success(Class<T> clazz) {
		return of(clazz, ApiCode.OK);
	}

	public static <T> ApiResponse<T> success(T data) {
		return of(data, ApiCode.OK);
	}

	public static <T> ApiResponse<T> failure(Class<T> clazz) {
		return of(clazz, ApiCode.FAILURE);
	}

	public static <T> ApiResponse<T> failure(T data) {
		return of(data, ApiCode.FAILURE);
	}

	public static <T> ApiResponse<T> of(Class<T> clazz, ApiCode code) {
		return ApiResponse.<T>builder()
				.success(code.isSuccess())
				.code(code)
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}

	public static <T> ApiResponse<T> of(T data, ApiCode code) {
		return ApiResponse.<T>builder()
				.success(code.isSuccess())
				.code(code)
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();
	}
}
