package com.dlut.simpleforum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.UserDto;
import com.dlut.simpleforum.service.UserService;

/**
 * @author Ishi_O
 * @since
 */
@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{uid}")
	public ApiResponse<UserDto> getUserProfile(@PathVariable Long uid) {
		return ApiResponse.success(UserDto.createUserDtoByUser(userService.getUser(uid)));
	}
}
