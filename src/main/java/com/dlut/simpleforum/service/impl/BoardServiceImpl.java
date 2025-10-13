package com.dlut.simpleforum.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.entity.Board;
import com.dlut.simpleforum.entity.User;
import com.dlut.simpleforum.repository.BoardRepository;
import com.dlut.simpleforum.repository.UserRepository;
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
	private final UserRepository userRepository;

	public BoardServiceImpl(BoardRepository boardRepository, UserRepository userRepository) {
		this.boardRepository = boardRepository;
		this.userRepository = userRepository;
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

	@Override
	public Board createBoard(String name, String description, Long uid) {
		Board board = new Board(name, description, userRepository.getReferenceById(uid));
		boardRepository.save(board);
		return board;
	}

	@Override
	public Board updateBoard(String name, String description, Long bid, Long uid, Long editorUid) {
		Board board = boardRepository.findById(bid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.board.not-found", null)));
		User editor = userRepository.findById(editorUid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.not-found", null)));
		board.checkPermission(editor);
		board.setName(name);
		board.setDescription(description);
		if (!uid.equals(editorUid)) {
			board.setModerator(
					// userRepository.findById(uid).orElseThrow(
					// () -> new
					// IllegalArgumentException(MessageSourceUtils.getMessage("error.user.not-found",
					// null)))
					userRepository.getReferenceById(uid));
		}
		return board;
	}

	@Override
	public void deleteBoard(Long bid, Long editorUid) {
		Board board = boardRepository.findById(bid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.board.not-found", null)));
		User editor = userRepository.findById(editorUid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.user.not-found", null)));
		board.checkPermission(editor);
		boardRepository.deleteById(bid);
	}
}
