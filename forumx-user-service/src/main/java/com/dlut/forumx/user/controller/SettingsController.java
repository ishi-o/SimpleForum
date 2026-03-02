package com.dlut.forumx.user.controller;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.forumx.commons.dto.ResultDTO;
import com.dlut.forumx.commons.dto.user.request.UpdateSettingsDTO;
import com.dlut.forumx.commons.dto.user.response.UserSettingsDTO;
import com.dlut.forumx.user.entity.UserSettings;
import com.dlut.forumx.user.service.SettingsService;
import com.dlut.forumx.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class SettingsController {

	private final SettingsService settingsService;
	private final UserService userService;

	private Long getCurrentUserId(Jwt jwt) {
		String keycloakId = jwt.getSubject();
		return userService.getUserIdByKeycloakId(keycloakId);
	}

	/**
	 * 获取用户设置
	 */
	@GetMapping("/settings/{userId}")
	public ResultDTO<UserSettingsDTO> getSettings(@PathVariable Long userId) {
		UserSettings settings = settingsService.getSettings(userId);

		UserSettingsDTO dto = new UserSettingsDTO();
		BeanUtils.copyProperties(settings, dto);
		dto.setUpdateTime(settings.getUpdateTime().format(DateTimeFormatter.ISO_DATE_TIME));

		return ResultDTO.success(dto);
	}

	/**
	 * 更新用户设置
	 */
	@PutMapping("/settings")
	public ResultDTO<Void> updateSettings(
			@AuthenticationPrincipal Jwt jwt,
			@RequestBody UpdateSettingsDTO dto) {

		Long userId = getCurrentUserId(jwt);

		UserSettings settings = settingsService.getSettings(userId);

		if (dto.getPrivacyProfile() != null)
			settings.setPrivacyProfile(dto.getPrivacyProfile());
		if (dto.getPrivacyPost() != null)
			settings.setPrivacyPost(dto.getPrivacyPost());
		if (dto.getNotificationLike() != null)
			settings.setNotificationLike(dto.getNotificationLike());
		if (dto.getNotificationComment() != null)
			settings.setNotificationComment(dto.getNotificationComment());
		if (dto.getNotificationFollow() != null)
			settings.setNotificationFollow(dto.getNotificationFollow());
		if (dto.getNotificationSystem() != null)
			settings.setNotificationSystem(dto.getNotificationSystem());
		if (dto.getEmailNotify() != null)
			settings.setEmailNotify(dto.getEmailNotify());

		settingsService.updateSettings(settings);

		return ResultDTO.success(null);
	}
}
