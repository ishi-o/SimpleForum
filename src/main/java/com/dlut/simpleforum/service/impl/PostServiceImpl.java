package com.dlut.simpleforum.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.entity.Post;
import com.dlut.simpleforum.repository.PostRepository;
import com.dlut.simpleforum.service.PostService;
import com.dlut.simpleforum.util.MessageSourceUtils;

import jakarta.transaction.Transactional;

/**
 * @author Ishi_O
 * @since
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;

	public PostServiceImpl(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Override
	public Slice<Post> getAllPostsByBoardId(Long bid, Integer pageNumber, Integer pageSize) {
		return postRepository.findAllByBoardBid(bid, PageRequest.of(pageNumber, pageSize,
				Sort.by(Order.desc("isPinned"), Order.desc("likes"), Order.desc("createdAt"))));
	}

	@Override
	public Post getSpecifiedPostByBoardId(Long pid, Long bid) {
		return postRepository.findByBoardBidAndPid(bid, pid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.post.not-found", null)));
	}

}
