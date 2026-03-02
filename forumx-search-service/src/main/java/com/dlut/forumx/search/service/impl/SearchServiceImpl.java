package com.dlut.forumx.search.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dlut.forumx.commons.dto.search.SearchRequestDTO;
import com.dlut.forumx.search.document.PostDocument;
import com.dlut.forumx.search.document.UserDocument;
import com.dlut.forumx.search.repository.PostRepository;
import com.dlut.forumx.search.repository.UserRepository;
import com.dlut.forumx.search.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Override
	public Page<PostDocument> searchPosts(SearchRequestDTO request) {
		// 构建分页请求
		Pageable pageable = buildPageable(request);

		// 有关键词
		if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
			// 有标签过滤
			if (request.getTags() != null && !request.getTags().isEmpty()) {
				return postRepository.findByTitleContainingOrContentContainingAndTagsIn(
						request.getKeyword(),
						request.getKeyword(),
						request.getTags(),
						pageable);
			}
			// 只有关键词
			return postRepository.findByTitleContainingOrContentContaining(
					request.getKeyword(),
					request.getKeyword(),
					pageable);
		}

		// 没有关键词，只有标签
		if (request.getTags() != null && !request.getTags().isEmpty()) {
			return postRepository.findByTagsIn(request.getTags(), pageable);
		}

		// 作者ID过滤
		if (request.getUserId() != null) {
			return postRepository.findByUserId(request.getUserId(), pageable);
		}

		// 没有任何条件，返回全部（一般不会这样）
		return postRepository.findAll(pageable);
	}

	@Override
	public Page<UserDocument> searchUsers(SearchRequestDTO request) {
		Pageable pageable = buildPageable(request);

		if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
			return userRepository.findByUsernameContainingOrNicknameContaining(
					request.getKeyword(),
					request.getKeyword(),
					pageable);
		}

		return userRepository.findAll(pageable);
	}

	/**
	 * 构建分页和排序
	 */
	private Pageable buildPageable(SearchRequestDTO request) {
		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortOrder()), request.getSortBy());
		return PageRequest.of(
				request.getPageNum() - 1,
				request.getPageSize(),
				sort);
	}
}
