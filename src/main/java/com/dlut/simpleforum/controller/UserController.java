package com.dlut.simpleforum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.simpleforum.dto.request.UserAuthRequest;
import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.UserLoginResponse;
import com.dlut.simpleforum.dto.result.UserLoginResult;
import com.dlut.simpleforum.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = { "localhost" })
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> register(
			@Valid @RequestParam("userAuth") UserAuthRequest userRegisterRequest) {
		try {
			userService.register(userRegisterRequest.getUsername(), userRegisterRequest.getPassword());
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.contentType(MediaType.APPLICATION_JSON)
					.body(ApiResponse.success(Void.class));
		} catch (Exception e) {
			return ResponseEntity
					.internalServerError()
					.contentType(MediaType.APPLICATION_JSON)
					.body(ApiResponse.failure(Void.class, e.getMessage()));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserLoginResponse>> login(
			@Valid @RequestParam("userAuth") UserAuthRequest userAuthRequest,
			HttpSession session) {
		try {
			UserLoginResult userLoginResult = userService.login(userAuthRequest.getUsername(),
					userAuthRequest.getPassword());
			session.setAttribute("userId", userLoginResult.getUid());
			session.setAttribute("userRole", userLoginResult.getRole());
			return ResponseEntity
					.status(HttpStatus.OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body(ApiResponse.success(
							UserLoginResponse.builder()
									.userLoginResult(userLoginResult)
									.build()));
		} catch (Exception e) {
			return ResponseEntity
					.internalServerError()
					.body(ApiResponse.failure(UserLoginResponse.class));
		}
	}

}
