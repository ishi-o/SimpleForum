package com.dlut.simpleforum.service;

import com.dlut.simpleforum.dto.result.PageResult;
import com.dlut.simpleforum.entity.Comment;
import com.dlut.simpleforum.entity.MainComment;
import com.dlut.simpleforum.entity.SubComment;

/**
 * @author Ishi_O
 * @since
 */
public interface CommentService {
	PageResult<MainComment> getAllMainComments(Long bid, Long pid, Integer pageNumber, Integer pageSize);

	PageResult<SubComment> getSpecifiedSubComments(Long bid, Long pid, Long cid, Integer pageNumber, Integer pageSize);

	Comment createMainComment(Long pid, Long uid, String content);

	SubComment createSubComment(Long pid, Long cid, Long uid, Long targetId, String content);
}
