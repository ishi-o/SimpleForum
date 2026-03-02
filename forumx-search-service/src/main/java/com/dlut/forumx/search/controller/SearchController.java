package com.dlut.forumx.search.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.forumx.commons.dto.PageDTO;
import com.dlut.forumx.commons.dto.ResultDTO;
import com.dlut.forumx.commons.dto.search.SearchRequestDTO;
import com.dlut.forumx.search.document.PostDocument;
import com.dlut.forumx.search.document.UserDocument;
import com.dlut.forumx.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	/**
	 * 搜索帖子
	 */
	@GetMapping("/posts")
	public ResultDTO<PageDTO<PostDocument>> searchPosts(SearchRequestDTO request) {
		Page<PostDocument> page = searchService.searchPosts(request);
		return ResultDTO.success(convertToPageDTO(page, request));
	}

	/**
	 * 搜索用户
	 */
	@GetMapping("/users")
	public ResultDTO<PageDTO<UserDocument>> searchUsers(SearchRequestDTO request) {
		Page<UserDocument> page = searchService.searchUsers(request);
		return ResultDTO.success(convertToPageDTO(page, request));
	}

	/**
	 * 将 Spring Data 的 Page 转换为 PageDTO
	 */
	private <T> PageDTO<T> convertToPageDTO(Page<T> page, SearchRequestDTO request) {
		PageDTO<T> pageDTO = new PageDTO<>();
		pageDTO.setList(page.getContent());
		pageDTO.setPageNum(request.getPageNum());
		pageDTO.setPageSize(request.getPageSize());
		pageDTO.setTotal(page.getTotalElements());
		pageDTO.setPages(page.getTotalPages());
		pageDTO.setHasNext(page.hasNext());
		pageDTO.setHasPrevious(page.hasPrevious());
		return pageDTO;
	}
}
