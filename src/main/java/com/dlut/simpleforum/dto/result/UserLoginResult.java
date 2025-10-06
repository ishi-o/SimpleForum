package com.dlut.simpleforum.dto.result;

import java.time.LocalDateTime;

import com.dlut.simpleforum.entity.User.UserRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResult {

	private Long uid;

	private String username;

	private UserRole role;

	private LocalDateTime createdAt;

	private Boolean need2FA;

}
