package com.dlut.forumx.content.service;

import java.util.List;

import com.dlut.forumx.content.dto.result.PageResult;
import com.dlut.forumx.content.entity.Post;
import com.dlut.forumx.content.entity.User.UserRole;

/**
 * @author Ishi_O
 * @since
 */
public interface PostService {
	PageResult<Post> getAllPostsByBoardId(Long bid, Integer pageNumber, Integer pageSize);

	Post getSpecifiedPostByBoardId(Long pid, Long bid);

	Post createPostByBoardId(Long bid, Long uid, String title, String content);

	Post trigglePin(Long bid, Long pid, Long uid, UserRole userRole);

	void removePost(Long bid, Long pid, Long uid, UserRole userRole);

	PageResult<Post> searchPostsByBoardId(List<String> keywords, Long bid, Integer pageNumber, Integer pageSize);
}
