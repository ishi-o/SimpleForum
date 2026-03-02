package com.dlut.forumx.user.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.forumx.commons.dto.ResultDTO;
import com.dlut.forumx.commons.dto.user.request.FollowStatusRequestDTO;
import com.dlut.forumx.commons.dto.user.response.FollowResultDTO;
import com.dlut.forumx.commons.dto.user.response.UserFollowDTO;
import com.dlut.forumx.user.entity.User;
import com.dlut.forumx.user.entity.UserFollow;
import com.dlut.forumx.user.service.FollowService;
import com.dlut.forumx.user.service.UserService;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FollowController {

	private final FollowService followService;
	private final UserService userService;

	private Long getCurrentUserId(Jwt jwt) {
		String keycloakId = jwt.getSubject();
		return userService.getUserIdByKeycloakId(keycloakId);
	}

	/**
	 * 关注用户
	 */
	@PostMapping("/follow/{targetId}")
	public ResultDTO<FollowResultDTO> follow(
			@AuthenticationPrincipal Jwt jwt,
			@PathVariable Long targetId) {

		Long userId = getCurrentUserId(jwt);

		if (userId.equals(targetId)) {
			return ResultDTO.error(400, "不能关注自己");
		}

		followService.follow(userId, targetId);

		FollowResultDTO result = new FollowResultDTO();
		result.setSuccess(true);
		result.setIsFollowing(true);
		result.setFollowCount(followService.getFollowingCount(userId));
		result.setFansCount(followService.getFollowerCount(targetId));

		return ResultDTO.success(result);
	}

	/**
	 * 取消关注
	 */
	@DeleteMapping("/unfollow/{targetId}")
	public ResultDTO<FollowResultDTO> unfollow(
			@AuthenticationPrincipal Jwt jwt,
			@PathVariable Long targetId) {

		Long userId = getCurrentUserId(jwt);

		followService.unfollow(userId, targetId);

		FollowResultDTO result = new FollowResultDTO();
		result.setSuccess(true);
		result.setIsFollowing(false);
		result.setFollowCount(followService.getFollowingCount(userId));
		result.setFansCount(followService.getFollowerCount(targetId));

		return ResultDTO.success(result);
	}

	/**
	 * 获取粉丝列表
	 */
	@GetMapping("/followers/{userId}")
	public ResultDTO<PageInfo<UserFollowDTO>> getFollowers(
			@PathVariable Long userId,
			@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "10") int pageSize,
			@AuthenticationPrincipal Jwt jwt) {

		Long currentUserId = jwt != null ? getCurrentUserId(jwt) : null;

		PageInfo<UserFollow> followPage = followService.getFollowers(userId, pageNum, pageSize);

		// 获取用户信息
		List<Long> followerIds = followPage.getList().stream()
				.map(UserFollow::getFollowerId)
				.collect(Collectors.toList());

		Map<Long, User> userMap = userService.getUsersByIds(followerIds).stream()
				.collect(Collectors.toMap(User::getId, u -> u));

		// 查询互关状态
		Map<Long, Boolean> mutualMap = currentUserId != null
				? followService.batchCheckFollowing(currentUserId, followerIds)
				: Map.of();

		// 转换为 DTO
		List<UserFollowDTO> dtoList = followPage.getList().stream()
				.map(follow -> {
					UserFollowDTO dto = new UserFollowDTO();
					User user = userMap.get(follow.getFollowerId());
					if (user != null) {
						BeanUtils.copyProperties(user, dto);
					}
					dto.setFollowTime(follow.getFollowTime().format(DateTimeFormatter.ISO_DATE_TIME));
					dto.setIsMutual(mutualMap.getOrDefault(follow.getFollowerId(), false));
					return dto;
				})
				.collect(Collectors.toList());

		// 包装分页
		PageInfo<UserFollowDTO> dtoPage = new PageInfo<>();
		BeanUtils.copyProperties(followPage, dtoPage, "list");
		dtoPage.setList(dtoList);

		return ResultDTO.success(dtoPage);
	}

	/**
	 * 获取关注列表
	 */
	@GetMapping("/following/{userId}")
	public ResultDTO<PageInfo<UserFollowDTO>> getFollowing(
			@PathVariable Long userId,
			@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "10") int pageSize,
			@AuthenticationPrincipal Jwt jwt) {

		Long currentUserId = jwt != null ? getCurrentUserId(jwt) : null;

		PageInfo<UserFollow> followPage = followService.getFollowing(userId, pageNum, pageSize);

		// 获取用户信息
		List<Long> followingIds = followPage.getList().stream()
				.map(UserFollow::getFollowedId)
				.collect(Collectors.toList());

		Map<Long, User> userMap = userService.getUsersByIds(followingIds).stream()
				.collect(Collectors.toMap(User::getId, u -> u));

		// 查询互关状态
		Map<Long, Boolean> mutualMap = currentUserId != null
				? followService.batchCheckFollowing(currentUserId, followingIds)
				: Map.of();

		// 转换为 DTO
		List<UserFollowDTO> dtoList = followPage.getList().stream()
				.map(follow -> {
					UserFollowDTO dto = new UserFollowDTO();
					User user = userMap.get(follow.getFollowedId());
					if (user != null) {
						BeanUtils.copyProperties(user, dto);
					}
					dto.setFollowTime(follow.getFollowTime().format(DateTimeFormatter.ISO_DATE_TIME));
					dto.setIsMutual(mutualMap.getOrDefault(follow.getFollowedId(), false));
					return dto;
				})
				.collect(Collectors.toList());

		PageInfo<UserFollowDTO> dtoPage = new PageInfo<>();
		BeanUtils.copyProperties(followPage, dtoPage, "list");
		dtoPage.setList(dtoList);

		return ResultDTO.success(dtoPage);
	}

	/**
	 * 批量查询关注状态
	 */
	@PostMapping("/follow/status")
	public ResultDTO<Map<Long, Boolean>> getFollowStatus(
			@AuthenticationPrincipal Jwt jwt,
			@RequestBody FollowStatusRequestDTO request) {

		Long userId = getCurrentUserId(jwt);
		Map<Long, Boolean> status = followService.batchCheckFollowing(userId, request.getTargetUserIds());
		return ResultDTO.success(status);
	}

	/**
	 * 内部接口：检查是否关注
	 */
	@GetMapping("/internal/follow/check")
	public ResultDTO<Boolean> isFollowing(
			@RequestParam Long followerId,
			@RequestParam Long followedId) {
		return ResultDTO.success(followService.isFollowing(followerId, followedId));
	}

	/**
	 * 内部接口：获取粉丝ID列表
	 */
	@GetMapping("/internal/follow/followers/{userId}")
	public ResultDTO<List<Long>> getFollowerIds(@PathVariable Long userId) {
		return ResultDTO.success(followService.getFollowerIds(userId));
	}

	/**
	 * 内部接口：获取关注ID列表
	 */
	@GetMapping("/internal/follow/following/{userId}")
	public ResultDTO<List<Long>> getFollowingIds(@PathVariable Long userId) {
		return ResultDTO.success(followService.getFollowingIds(userId));
	}
}
