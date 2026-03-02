package com.dlut.forumx.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.dlut.forumx.search.document.PostDocument;

@Repository
public interface PostRepository extends ElasticsearchRepository<PostDocument, Long> {

	/**
	 * 按关键词搜索帖子（标题或内容）
	 */
	Page<PostDocument> findByTitleContainingOrContentContaining(
			String title, String content, Pageable pageable);

	/**
	 * 按标签过滤
	 */
	Page<PostDocument> findByTagsIn(java.util.List<String> tags, Pageable pageable);

	/**
	 * 按作者ID查询
	 */
	Page<PostDocument> findByUserId(Long userId, Pageable pageable);

	/**
	 * 组合查询：关键词 + 标签
	 */
	Page<PostDocument> findByTitleContainingOrContentContainingAndTagsIn(
			String title, String content, java.util.List<String> tags, Pageable pageable);
}
