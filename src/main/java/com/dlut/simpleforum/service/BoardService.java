package com.dlut.simpleforum.service;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.entity.Board;

/**
 * @author Ishi_O
 * @since
 */
@Service
public interface BoardService {
	Slice<Board> getAllBoards(Integer pageNumber, Integer pageSize);

	Board getSpecifiedBoard(Long bid);

	Board createBoard(String name, String description, Long uid);

	Board updateBoard(String name, String description, Long bid, Long uid, Long editorUid);

	void deleteBoard(Long bid, Long editorUid);
}
