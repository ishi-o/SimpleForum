package com.dlut.simpleforum.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.entity.Board;
import com.dlut.simpleforum.entity.Post;
import com.dlut.simpleforum.entity.User.UserRole;
import com.dlut.simpleforum.repository.BoardRepository;
import com.dlut.simpleforum.repository.PostRepository;
import com.dlut.simpleforum.repository.UserRepository;
import com.dlut.simpleforum.service.PostService;
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
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;

	public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
			BoardRepository boardRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.boardRepository = boardRepository;
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

	@Override
	public Post createPostByBoardId(Long bid, Long uid, String title, String content) {
		Post post = new Post(title, content, userRepository.getReferenceById(uid),
				boardRepository.getReferenceById(bid));
		post = postRepository.save(post);
		return post;
	}

	@Override
	public void trigglePin(Long bid, Long pid, Long uid, UserRole userRole) {
		Board board = boardRepository.findById(bid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.board.not-found", null)));
		Post post = postRepository.findByBoardBidAndPid(bid, pid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.post.not-found", null)));
		if (!board.getModerator().getUid().equals(uid)) {
			PermissionUtils.isNotRoleThenThrow(UserRole.ADMIN, userRole);
		}
		post.trigglePin();
	}
}
