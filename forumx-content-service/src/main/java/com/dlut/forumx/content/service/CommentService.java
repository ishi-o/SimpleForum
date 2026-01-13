package com.dlut.forumx.content.service;

import com.dlut.forumx.content.dto.result.PageResult;
import com.dlut.forumx.content.entity.Comment;
import com.dlut.forumx.content.entity.MainComment;
import com.dlut.forumx.content.entity.SubComment;

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
