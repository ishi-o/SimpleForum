package com.dlut.simpleforum.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dlut.simpleforum.common.exception.auth.AuthenticationException;
import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.ErrorResponse;

@RestControllerAdvice
public class ExceptionHandlerController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ApiResponse<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		StringBuilder errors = new StringBuilder();
		for (ObjectError error : bindingResult.getAllErrors()) {
			errors.append(error.getCode() + ":" + error.getDefaultMessage() + "\n");
		}
		return ApiResponse.failure(
				ErrorResponse.of(errors.toString()));
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler({ AuthenticationException.class })
	public ApiResponse<ErrorResponse> handleAuthenticationException(AuthenticationException exception) {
		return ApiResponse.failure(ErrorResponse.from(exception));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ IllegalArgumentException.class, SQLException.class })
	public ApiResponse<ErrorResponse> handleIllegalArgumentException(Exception e) {
		return ApiResponse.failure(
				ErrorResponse
						.builder()
						.message(e.getMessage())
						.build());
	}
}
