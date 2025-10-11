package com.dlut.simpleforum.service;

import org.springframework.data.domain.Slice;

import com.dlut.simpleforum.entity.Post;

/**
 * @author Ishi_O
 * @since
 */
public interface PostService {
	Slice<Post> getAllPostsByBoardId(Long bid, Integer pageNumber, Integer pageSize);

	Post getSpecifiedPostByBoardId(Long pid, Long bid);
}
