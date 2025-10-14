package com.dlut.simpleforum.service;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.entity.Board;
import com.dlut.simpleforum.entity.User.UserRole;

/**
 * @author Ishi_O
 * @since
 */
@Service
public interface BoardService {
	Slice<Board> getAllBoards(Integer pageNumber, Integer pageSize);

	Slice<Board> getLikelyBoards(List<String> keywords, Integer pageNumber, Integer pageSize);

	Board getSpecifiedBoard(Long bid);

	Board createBoard(String name, String description, Long uid);

	Board updateBoard(Long bid, String name, String description, Long uid, Long editorUid, UserRole userRole);

	void deleteBoard(Long bid, Long editorUid, UserRole editorRole);
}
