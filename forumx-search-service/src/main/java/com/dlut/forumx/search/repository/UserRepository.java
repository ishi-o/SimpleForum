package com.dlut.forumx.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.dlut.forumx.search.document.UserDocument;

@Repository
public interface UserRepository extends ElasticsearchRepository<UserDocument, Long> {
	/**
	 * 按关键词搜索用户（用户名或昵称）
	 */
	Page<UserDocument> findByUsernameContainingOrNicknameContaining(
			String username, String nickname, Pageable pageable);

	/**
	 * 按用户名精确匹配
	 */
	Page<UserDocument> findByUsername(String username, Pageable pageable);
}
