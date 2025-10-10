package com.dlut.simpleforum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.dlut.simpleforum.dto.request.UserAuthRequest;
import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.UserLoginResponse;
import com.dlut.simpleforum.entity.User;
import com.dlut.simpleforum.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/register")
	public ApiResponse<Void> register(
			@Valid @RequestBody UserAuthRequest userRegisterRequest) {
		userService.register(userRegisterRequest.getUsername(), userRegisterRequest.getPassword());
		return ApiResponse.success(Void.class);
	}

	@PostMapping("/login")
	public ApiResponse<UserLoginResponse> login(
			@Valid @RequestBody UserAuthRequest userAuthRequest,
			HttpSession session) {
		User user = userService.login(userAuthRequest.getUsername(),
				userAuthRequest.getPassword());
		session.setAttribute("userId", user.getUid());
		session.setAttribute("userRole", user.getRole());
		return ApiResponse.success(
				UserLoginResponse.builder()
						.uid(user.getUid())
						.username(user.getName())
						.createdAt(user.getCreatedAt())
						.need2FA(false)
						.build());
	}

	@PostMapping("/logout")
	public ApiResponse<Void> logout(@SessionAttribute("userId") Long uid) {
		userService.logout(uid);
		return ApiResponse.success(Void.class);
	}

}
