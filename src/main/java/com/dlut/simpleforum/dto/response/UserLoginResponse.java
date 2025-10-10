package com.dlut.simpleforum.dto.response;

import java.time.LocalDateTime;

import com.dlut.simpleforum.entity.User.UserRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {

	private Long uid;

	private String username;

	private UserRole role;

	private LocalDateTime createdAt;

	private Boolean need2FA;

}
