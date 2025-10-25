package com.dlut.simpleforum.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.dto.result.PageResult;
import com.dlut.simpleforum.entity.Comment;
import com.dlut.simpleforum.entity.MainComment;
import com.dlut.simpleforum.entity.Post;
import com.dlut.simpleforum.entity.SubComment;
import com.dlut.simpleforum.entity.User;
import com.dlut.simpleforum.repository.CommentRepository;
import com.dlut.simpleforum.service.CommentService;
import com.dlut.simpleforum.utils.EntityReferenceUtils;

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

	@Cacheable(cacheNames = "comments:page", key = "#pid + ':' + #pageNumber + ':' + #pageSize")
	@Override
	public PageResult<MainComment> getAllMainComments(Long bid, Long pid, Integer pageNumber, Integer pageSize) {
		return PageResult.from(commentRepository.findMainCommentsByPostPidAndPostBoardBid(pid, bid,
				PageRequest.of(pageNumber, pageSize)));
	}

	@Cacheable(cacheNames = "comments:sub:page", key = "#cid + ':' + #pageNumber + ':' + #pageSize")
	@Override
	public PageResult<SubComment> getSpecifiedSubComments(Long bid, Long pid, Long cid, Integer pageNumber,
			Integer pageSize) {
		return PageResult.from(commentRepository.findSubCommentsAllByPostPidAndPostBoardBidAndParentCid(pid, bid, cid,
				PageRequest.of(pageNumber, pageSize)));
	}

	@CacheEvict(cacheNames = "comments:page", allEntries = true)
	@Override
	public Comment createMainComment(Long pid, Long uid, String content) {
		Comment cmt = new MainComment(content,
				EntityReferenceUtils.getReferenceById(User.class, uid),
				EntityReferenceUtils.getReferenceById(Post.class, pid));
		cmt = commentRepository.save(cmt);
		return cmt;
	}

	@CacheEvict(cacheNames = "comments:sub:page", allEntries = true)
	@Override
	public SubComment createSubComment(Long pid, Long cid, Long uid, Long targetId, String content) {
		SubComment cmt = new SubComment(content,
				EntityReferenceUtils.getReferenceById(User.class, uid),
				EntityReferenceUtils.getReferenceById(MainComment.class, cid),
				EntityReferenceUtils.getReferenceById(SubComment.class, targetId));
		cmt = commentRepository.save(cmt);
		return cmt;
	}
}
