package com.dlut.simpleforum.dto.response;

import java.time.LocalDateTime;

import com.dlut.simpleforum.common.ApiCode;
import com.dlut.simpleforum.util.MessageSourceUtils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
	private Boolean success;
	private ApiCode code;
	private String message;
	private T data;
	private LocalDateTime timestamp;

	public static <T> ApiResponse<T> success(T data) {
		return of(data, ApiCode.OK);
	}

	public static ApiResponse<Void> success() {
		return of(ApiCode.OK);
	}

	public static ApiResponse<Void> failure() {
		return of(ApiCode.FAILURE);
	}

	public static <T> ApiResponse<T> failure(T data) {
		return of(data, ApiCode.FAILURE);
	}

	public static ApiResponse<Void> of(ApiCode code) {
		return ApiResponse.<Void>builder()
				.success(code.isSuccess())
				.code(code)
				.message(MessageSourceUtils.getMessage(code.getMessageCode(), null))
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}

	public static <T> ApiResponse<T> of(T data, ApiCode code) {
		return ApiResponse.<T>builder()
				.success(code.isSuccess())
				.code(code)
				.message(MessageSourceUtils.getMessage(code.getMessageCode(), null))
				.data(data)
				.timestamp(LocalDateTime.now())
				.build();
	}
}
