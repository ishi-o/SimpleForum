package com.dlut.simpleforum.service;

import org.springframework.data.domain.Slice;

import com.dlut.simpleforum.entity.Post;
import com.dlut.simpleforum.entity.User.UserRole;

/**
 * @author Ishi_O
 * @since
 */
public interface PostService {
	Slice<Post> getAllPostsByBoardId(Long bid, Integer pageNumber, Integer pageSize);

	Post getSpecifiedPostByBoardId(Long pid, Long bid);

	Post createPostByBoardId(Long bid, Long uid, String title, String content);

	void trigglePin(Long bid, Long pid, Long uid, UserRole userRole);
}
