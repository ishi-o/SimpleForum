package com.dlut.forumx.user.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.forumx.commons.dto.ResultDTO;
import com.dlut.forumx.commons.dto.user.request.UpdateTagsRequestDTO;
import com.dlut.forumx.commons.dto.user.response.HotTagDTO;
import com.dlut.forumx.commons.dto.user.response.UserTagDTO;
import com.dlut.forumx.user.entity.HotTag;
import com.dlut.forumx.user.entity.UserTag;
import com.dlut.forumx.user.service.TagService;
import com.dlut.forumx.user.service.UserService;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class TagController {

	private final TagService tagService;
	private final UserService userService;

	private Long getCurrentUserId(Jwt jwt) {
		String keycloakId = jwt.getSubject();
		return userService.getUserIdByKeycloakId(keycloakId);
	}

	/**
	 * 获取用户标签
	 */
	@GetMapping("/tags/{userId}")
	public ResultDTO<List<UserTagDTO>> getUserTags(@PathVariable Long userId) {
		List<UserTag> tags = tagService.getUserTags(userId);

		List<UserTagDTO> dtoList = tags.stream()
				.map(tag -> {
					UserTagDTO dto = new UserTagDTO();
					BeanUtils.copyProperties(tag, dto);
					return dto;
				})
				.collect(Collectors.toList());

		return ResultDTO.success(dtoList);
	}

	/**
	 * 更新用户标签
	 */
	@PutMapping("/tags")
	public ResultDTO<Void> updateTags(
			@AuthenticationPrincipal Jwt jwt,
			@RequestBody UpdateTagsRequestDTO request) {

		Long userId = getCurrentUserId(jwt);
		tagService.updateUserTags(userId, request.getTags());
		return ResultDTO.success(null);
	}

	/**
	 * 获取热门标签
	 */
	@GetMapping("/tags/hot")
	public ResultDTO<PageInfo<HotTagDTO>> getHotTags(
			@RequestParam(defaultValue = "1") int pageNum,
			@RequestParam(defaultValue = "20") int pageSize) {

		PageInfo<HotTag> tagPage = tagService.getHotTags(pageNum, pageSize);

		List<HotTagDTO> dtoList = tagPage.getList().stream()
				.map(tag -> {
					HotTagDTO dto = new HotTagDTO();
					BeanUtils.copyProperties(tag, dto);
					return dto;
				})
				.collect(Collectors.toList());

		PageInfo<HotTagDTO> dtoPage = new PageInfo<>();
		BeanUtils.copyProperties(tagPage, dtoPage, "list");
		dtoPage.setList(dtoList);

		return ResultDTO.success(dtoPage);
	}

	/**
	 * 内部接口：增加标签用户计数
	 */
	@PostMapping("/internal/tags/user/{tagName}")
	public ResultDTO<Void> incrTagUserCount(@PathVariable String tagName) {
		tagService.incrTagUserCount(tagName);
		return ResultDTO.success(null);
	}

	/**
	 * 内部接口：获取标签下的用户
	 */
	@GetMapping("/internal/tags/{tagName}/users")
	public ResultDTO<List<Long>> getUserIdsByTag(
			@PathVariable String tagName,
			@RequestParam(defaultValue = "100") int limit) {
		return ResultDTO.success(tagService.getUserIdsByTag(tagName, limit));
	}
}
