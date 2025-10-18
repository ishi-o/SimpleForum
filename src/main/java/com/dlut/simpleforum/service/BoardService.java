package com.dlut.simpleforum.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dlut.simpleforum.dto.result.PageResult;
import com.dlut.simpleforum.entity.Board;
import com.dlut.simpleforum.entity.User.UserRole;

/**
 * @author Ishi_O
 * @since
 */
@Service
public interface BoardService {
	PageResult<Board> getAllBoards(Integer pageNumber, Integer pageSize);

	PageResult<Board> getLikelyBoards(List<String> keywords, Integer pageNumber, Integer pageSize);

	PageResult<Board> getBoardsByUid(Long uid, Integer pageNumber, Integer pageSize);

	Board getSpecifiedBoard(Long bid);

	Board createBoard(String name, String description, Long uid);

	Board updateBoard(Long bid, String name, String description, Long uid, Long editorUid, UserRole userRole);

	void deleteBoard(Long bid, Long editorUid, UserRole editorRole);
}
