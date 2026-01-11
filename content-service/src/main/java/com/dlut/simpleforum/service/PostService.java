package com.dlut.simpleforum.service;

import java.util.List;

import com.dlut.simpleforum.dto.result.PageResult;
import com.dlut.simpleforum.entity.Post;
import com.dlut.simpleforum.entity.User.UserRole;

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
