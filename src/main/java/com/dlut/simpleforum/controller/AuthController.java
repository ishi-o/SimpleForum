package com.dlut.simpleforum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.dlut.simpleforum.common.session.SessionUser;
import com.dlut.simpleforum.dto.request.UserAuthRequest;
import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.UserDto;
import com.dlut.simpleforum.entity.User;
import com.dlut.simpleforum.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

	private final AuthenticationService authenticationService;

	@GetMapping("/me")
	public ApiResponse<UserDto> getMe(
			@SessionAttribute SessionUser sessionUser) {
		User user = authenticationService.getMe(sessionUser);
		return ApiResponse.success(UserDto.createUserDtoByUser(user));
	}

	@PostMapping("/login")
	public ApiResponse<UserDto> login(
			@Valid @RequestBody UserAuthRequest userAuthRequest,
			@SessionAttribute SessionUser sessionUser) {
		User user = authenticationService.login(
				sessionUser,
				userAuthRequest.getUsername(),
				userAuthRequest.getPassword());
		return ApiResponse.success(UserDto.createUserDtoByUser(user));
	}

	@PostMapping("/logout")
	public ApiResponse<Void> logout(
			@SessionAttribute SessionUser sessionUser) {
		authenticationService.logout(sessionUser);
		return ApiResponse.success();
	}
}
