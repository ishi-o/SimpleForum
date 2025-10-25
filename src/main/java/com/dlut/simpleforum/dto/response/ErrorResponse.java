package com.dlut.simpleforum.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 */
@Data
@Builder
public class ErrorResponse {

	private String message;

	public static ErrorResponse from(Exception ex) {
		return builder()
				.message(ex.getMessage())
				.build();
	}

	public static ErrorResponse of(String msg) {
		return builder()
				.message(msg)
				.build();
	}
}
