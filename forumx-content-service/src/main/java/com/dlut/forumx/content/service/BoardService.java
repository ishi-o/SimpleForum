package com.dlut.forumx.content.service;

import java.util.List;

import com.dlut.forumx.common.content.dto.result.PageResult;
import com.dlut.forumx.content.entity.Board;

/**
 * @author Ishi_O
 * @since
 */
public interface BoardService {
	PageResult<Board> getAllBoards(Integer pageNumber, Integer pageSize);

	PageResult<Board> getLikelyBoards(List<String> keywords, Integer pageNumber, Integer pageSize);

	PageResult<Board> getBoardsByUid(Long uid, Integer pageNumber, Integer pageSize);

	Board getSpecifiedBoard(Long bid);

	Board createBoard(String name, String description, Long uid);

	Board updateBoard(Long bid, String name, String description, Long uid, Long editorUid, UserRole userRole);

	void deleteBoard(Long bid, Long editorUid, UserRole editorRole);
}
