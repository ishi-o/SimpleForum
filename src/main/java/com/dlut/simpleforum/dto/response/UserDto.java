package com.dlut.simpleforum.dto.response;

import java.time.LocalDateTime;

import com.dlut.simpleforum.common.enums.UserRole;
import com.dlut.simpleforum.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

	private Long uid;

	private String username;

	private UserRole role;

	private LocalDateTime createdAt;

	private Boolean need2FA;

	public static UserDto createUserDtoByUser(User user) {
		return UserDto.builder()
				.uid(user.getUid())
				.username(user.getName())
				.role(user.getRole())
				.createdAt(user.getCreatedAt())
				.need2FA(false)
				.build();
	}
}
