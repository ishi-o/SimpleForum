package com.dlut.simpleforum.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.simpleforum.dto.request.UserAuthRequest;
import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.UserLoginResponse;
import com.dlut.simpleforum.entity.User;
import com.dlut.simpleforum.entity.User.UserRole;
import com.dlut.simpleforum.service.UserService;
import com.dlut.simpleforum.util.MessageSourceUtils;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
		return ApiResponse.success();
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
						.role(user.getRole())
						.createdAt(user.getCreatedAt())
						.need2FA(false)
						.build());
	}

	@PostMapping("/logout")
	public ApiResponse<Void> logout(HttpSession session) {
		if (session.getAttribute("userId") == null) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.offline", null));
		}
		userService.logout((Long) session.getAttribute("userId"));
		session.removeAttribute("userId");
		session.removeAttribute("userRole");
		return ApiResponse.success();
	}

	@PostMapping("/guest")
	public ApiResponse<String> guest(HttpSession session) {
		String guestId = (String) session.getAttribute("userId");
		if (guestId == null) {
			guestId = "guest_" + UUID.randomUUID();
			session.setAttribute("userId", guestId);
			session.setAttribute("userRole", UserRole.GUEST);
		}
		return ApiResponse.success(guestId);
	}
}
