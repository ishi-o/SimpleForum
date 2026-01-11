package com.dlut.simpleforum.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.dto.result.PageResult;
import com.dlut.simpleforum.entity.Board;
import com.dlut.simpleforum.entity.Post;
import com.dlut.simpleforum.entity.User;
import com.dlut.simpleforum.entity.User.UserRole;
import com.dlut.simpleforum.repository.BoardRepository;
import com.dlut.simpleforum.repository.PostRepository;
import com.dlut.simpleforum.service.PostService;
import com.dlut.simpleforum.util.EntityReferenceUtils;
import com.dlut.simpleforum.util.MessageSourceUtils;
import com.dlut.simpleforum.util.PermissionUtils;

import jakarta.transaction.Transactional;

/**
 * @author Ishi_O
 * @since
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
	private final BoardRepository boardRepository;

	public PostServiceImpl(PostRepository postRepository,
			BoardRepository boardRepository) {
		this.postRepository = postRepository;
		this.boardRepository = boardRepository;
	}

	@Cacheable(cacheNames = "posts:page", key = "#bid + ':' + #pageNumber + ':' + #pageSize")
	@Override
	public PageResult<Post> getAllPostsByBoardId(Long bid, Integer pageNumber, Integer pageSize) {
		return PageResult.from(postRepository.findAllByBoardBid(bid, PageRequest.of(pageNumber, pageSize,
				Sort.by(Order.desc("isPinned"), Order.desc("likes"), Order.desc("createdAt")))));
	}

	@Cacheable(cacheNames = "posts", key = "#pid")
	@Override
	public Post getSpecifiedPostByBoardId(Long pid, Long bid) {
		return postRepository.findByBoardBidAndPid(bid, pid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.post.not-found", null)));
	}

	@CacheEvict(cacheNames = "posts:page", allEntries = true)
	@Override
	public Post createPostByBoardId(Long bid, Long uid, String title, String content) {
		Post post = new Post(title, content, EntityReferenceUtils.getReferenceById(User.class, uid),
				boardRepository.getReferenceById(bid));
		post = postRepository.save(post);
		return post;
	}

	@CachePut(cacheNames = "posts", key = "#pid")
	@CacheEvict(cacheNames = { "posts:page" }, allEntries = true)
	@Override
	public Post trigglePin(Long bid, Long pid, Long uid, UserRole userRole) {
		Board board = boardRepository.findById(bid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.board.not-found", null)));
		Post post = postRepository.findByBoardBidAndPid(bid, pid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.post.not-found", null)));
		if (!board.getModerator().getUid().equals(uid)) {
			PermissionUtils.isNotRoleThenThrow(UserRole.ADMIN, userRole);
		}
		post.trigglePin();
		return post;
	}

	@Caching(evict = {
			@CacheEvict(cacheNames = "posts:page", allEntries = true),
			@CacheEvict(cacheNames = "posts", key = "#pid")
	})
	@Override
	public void removePost(Long bid, Long pid, Long uid, UserRole userRole) {
		Board board = boardRepository.findById(bid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.board.not-found", null)));
		if (!board.getModerator().getUid().equals(uid)) {
			PermissionUtils.isNotRoleThenThrow(UserRole.ADMIN, userRole);
		}
		postRepository.deleteById(pid);
	}

	@Override
	public PageResult<Post> searchPostsByBoardId(List<String> keywords, Long bid, Integer pageNumber,
			Integer pageSize) {
		return PageResult.from(postRepository.findByTitleOrContentContaining(bid, keywords.stream()
				.collect(Collectors.joining("%")), PageRequest.of(pageNumber, pageSize)));
	}
}
