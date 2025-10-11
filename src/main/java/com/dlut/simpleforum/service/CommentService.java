package com.dlut.simpleforum.service;

import org.springframework.data.domain.Slice;

import com.dlut.simpleforum.entity.MainComment;
import com.dlut.simpleforum.entity.SubComment;

/**
 * @author Ishi_O
 * @since
 */
public interface CommentService {
	Slice<MainComment> getAllMainComments(Long bid, Long pid, Integer pageNumber, Integer pageSize);

	Slice<SubComment> getSpecifiedSubComments(Long bid, Long pid, Long cid, Integer pageNumber, Integer pageSize);
}
