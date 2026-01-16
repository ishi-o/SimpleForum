package com.dlut.forumx.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.forumx.common.api.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// TODO: account request
@RestController
@RequestMapping("/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

	@GetMapping("/me")
	public ApiResponse<Void> getMe() {
		return ApiResponse.success();
	}

	@PostMapping("/password")
	public ApiResponse<Void> updatePassword() {
		return ApiResponse.success();
	}

	@PutMapping("/profile")
	public ApiResponse<Void> updateProfile() {
		return ApiResponse.success();
	}

	@DeleteMapping("/me")
	public ApiResponse<Void> deleteMe() {
		return ApiResponse.success();
	}
}
