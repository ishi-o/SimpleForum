package com.dlut.simpleforum.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.entity.Board;
import com.dlut.simpleforum.repository.BoardRepository;
import com.dlut.simpleforum.service.BoardService;
import com.dlut.simpleforum.util.MessageSourceUtils;

import jakarta.transaction.Transactional;

/**
 * @author Ishi_O
 * @since
 */
@Service
@Transactional
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;

	public BoardServiceImpl(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	@Override
	public Slice<Board> getAllBoards(Integer pageNumber, Integer pageSize) {
		return boardRepository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public Board getSpecifiedBoard(Long bid) {
		return boardRepository.findById(bid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.board.not-found", null)));
	}
}
