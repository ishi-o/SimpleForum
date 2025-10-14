package com.dlut.simpleforum.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.entity.MainComment;
import com.dlut.simpleforum.entity.SubComment;
import com.dlut.simpleforum.repository.CommentRepository;
import com.dlut.simpleforum.service.CommentService;

import jakarta.transaction.Transactional;

/**
 * @author Ishi_O
 * @since
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;

	public CommentServiceImpl(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	@Override
	public Slice<MainComment> getAllMainComments(Long bid, Long pid, Integer pageNumber, Integer pageSize) {
		return commentRepository.findMainCommentsByPostPidAndPostBoardBid(pid, bid,
				PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public Slice<SubComment> getSpecifiedSubComments(Long bid, Long pid, Long cid, Integer pageNumber,
			Integer pageSize) {
		return commentRepository.findSubCommentsAllByPostPidAndPostBoardBidAndParentCid(pid, bid, cid,
				PageRequest.of(pageNumber, pageSize));
	}
}
