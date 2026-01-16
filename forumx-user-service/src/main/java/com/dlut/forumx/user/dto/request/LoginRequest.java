package com.dlut.forumx.user.dto.request;

import lombok.Builder;
import lombok.Data;

// TODO: login request dto
@Data
@Builder
public class LoginRequest {
	private String account;

	private String credential;
}
