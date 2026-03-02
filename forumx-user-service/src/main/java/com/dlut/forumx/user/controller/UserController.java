package com.dlut.forumx.user.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dlut.forumx.commons.dto.ResultDTO;
import com.dlut.forumx.commons.dto.user.request.UpdateProfileDTO;
import com.dlut.forumx.commons.dto.user.response.AvatarDTO;
import com.dlut.forumx.commons.dto.user.response.UserProfileDTO;
import com.dlut.forumx.commons.dto.user.response.UserStatsDTO;
import com.dlut.forumx.user.entity.User;
import com.dlut.forumx.user.entity.UserStats;
import com.dlut.forumx.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * 辅助方法：从JWT获取当前用户
	 */
	private User getCurrentUserFromJwt(Jwt jwt) {
		if (jwt == null)
			return null;
		String keycloakId = jwt.getSubject();
		return userService.getUserByKeycloakId(keycloakId);
	}

	/**
	 * 辅助方法：User转UserProfileDTO
	 */
	private UserProfileDTO toUserProfileDTO(User user, UserStats stats, Boolean isFollowed) {
		UserProfileDTO dto = new UserProfileDTO();
		BeanUtils.copyProperties(user, dto);

		if (user.getBirthday() != null) {
			dto.setBirthday(user.getBirthday().format(DateTimeFormatter.ISO_DATE));
		}
		dto.setRegisterTime(user.getRegisterTime().format(DateTimeFormatter.ISO_DATE_TIME));

		if (stats != null) {
			dto.setFollowCount(stats.getFollowCount());
			dto.setFansCount(stats.getFansCount());
			dto.setPostCount(stats.getPostCount());
		}

		dto.setIsFollowed(isFollowed);
		return dto;
	}

	/**
	 * 获取当前登录用户信息
	 */
	@GetMapping("/me")
	public ResultDTO<UserProfileDTO> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
		User user = getCurrentUserFromJwt(jwt);
		if (user == null) {
			return ResultDTO.error(404, "用户不存在");
		}

		UserStats stats = userService.getUserStats(user.getId());
		UserProfileDTO dto = toUserProfileDTO(user, stats, false);

		return ResultDTO.success(dto);
	}

	/**
	 * 获取用户资料
	 */
	@GetMapping("/profile/{userId}")
	public ResultDTO<UserProfileDTO> getProfile(
			@PathVariable Long userId,
			@AuthenticationPrincipal Jwt jwt) {

		User user = userService.getUserById(userId);
		if (user == null) {
			return ResultDTO.error(404, "用户不存在");
		}

		UserStats stats = userService.getUserStats(userId);

		// 当前用户是否关注了目标用户（后续由FollowService实现）
		Boolean isFollowed = false;

		UserProfileDTO dto = toUserProfileDTO(user, stats, isFollowed);
		return ResultDTO.success(dto);
	}

	/**
	 * 更新用户资料
	 */
	@PutMapping("/profile")
	public ResultDTO<UserProfileDTO> updateProfile(
			@AuthenticationPrincipal Jwt jwt,
			@RequestBody UpdateProfileDTO dto) {

		User user = getCurrentUserFromJwt(jwt);
		if (user == null) {
			return ResultDTO.error(404, "用户不存在");
		}

		// 更新字段
		if (dto.getNickname() != null)
			user.setNickname(dto.getNickname());
		if (dto.getBio() != null)
			user.setBio(dto.getBio());
		if (dto.getGender() != null)
			user.setGender(dto.getGender());
		if (dto.getBirthday() != null) {
			user.setBirthday(LocalDate.parse(dto.getBirthday()));
		}
		if (dto.getLocation() != null)
			user.setLocation(dto.getLocation());
		if (dto.getWebsite() != null)
			user.setWebsite(dto.getWebsite());
		if (dto.getPhone() != null)
			user.setPhone(dto.getPhone());

		userService.updateUser(user);

		UserStats stats = userService.getUserStats(user.getId());
		UserProfileDTO result = toUserProfileDTO(user, stats, false);

		return ResultDTO.success(result);
	}

	/**
	 * 上传头像
	 */
	@PostMapping("/avatar")
	public ResultDTO<AvatarDTO> uploadAvatar(
			@AuthenticationPrincipal Jwt jwt,
			@RequestParam("file") MultipartFile file) {

		User user = getCurrentUserFromJwt(jwt);
		if (user == null) {
			return ResultDTO.error(404, "用户不存在");
		}

		// TODO: 调用 file-service 上传，获取URL
		String avatarUrl = "http://localhost:9000/avatars/" + user.getId() + ".jpg";

		user.setAvatar(avatarUrl);
		userService.updateUser(user);

		AvatarDTO dto = new AvatarDTO();
		dto.setUrl(avatarUrl);
		dto.setThumbnailUrl(avatarUrl + "?thumbnail=true");

		return ResultDTO.success(dto);
	}

	/**
	 * 删除头像
	 */
	@DeleteMapping("/avatar")
	public ResultDTO<Void> deleteAvatar(@AuthenticationPrincipal Jwt jwt) {
		User user = getCurrentUserFromJwt(jwt);
		if (user == null) {
			return ResultDTO.error(404, "用户不存在");
		}

		user.setAvatar("");
		userService.updateUser(user);

		return ResultDTO.success(null);
	}

	/**
	 * 获取用户统计
	 */
	@GetMapping("/stats/{userId}")
	public ResultDTO<UserStatsDTO> getUserStats(@PathVariable Long userId) {
		UserStats stats = userService.getUserStats(userId);
		if (stats == null) {
			return ResultDTO.error(404, "统计信息不存在");
		}

		UserStatsDTO dto = new UserStatsDTO();
		BeanUtils.copyProperties(stats, dto);
		return ResultDTO.success(dto);
	}

	/**
	 * 内部接口：根据KeycloakID获取业务ID
	 */
	@GetMapping("/internal/keycloak/{keycloakId}")
	public ResultDTO<Long> getUserIdByKeycloakId(@PathVariable String keycloakId) {
		Long userId = userService.getUserIdByKeycloakId(keycloakId);
		return userId != null ? ResultDTO.success(userId) : ResultDTO.error(404, "用户不存在");
	}
}
