package com.dlut.forumx.search.service;

import org.springframework.data.domain.Page;

import com.dlut.forumx.commons.dto.search.SearchRequestDTO;
import com.dlut.forumx.search.document.PostDocument;
import com.dlut.forumx.search.document.UserDocument;

public interface SearchService {

	/**
	 * 搜索帖子，返回 ES 原始文档
	 */
	Page<PostDocument> searchPosts(SearchRequestDTO request);

	/**
	 * 搜索用户，返回 ES 原始文档
	 */
	Page<UserDocument> searchUsers(SearchRequestDTO request);
}
