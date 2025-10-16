package com.dlut.simpleforum.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dlut.simpleforum.entity.Board;
import com.dlut.simpleforum.entity.User.UserRole;
import com.dlut.simpleforum.repository.BoardRepository;
import com.dlut.simpleforum.repository.UserRepository;
import com.dlut.simpleforum.service.BoardService;
import com.dlut.simpleforum.util.MessageSourceUtils;
import com.dlut.simpleforum.util.PermissionUtils;

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
	public Slice<Board> getLikelyBoards(List<String> keywords, Integer pageNumber, Integer pageSize) {
		String keywordPattern = keywords.stream().collect(Collectors.joining("%"));
		return boardRepository.findByNameOrDescriptionContaining(keywordPattern,
				PageRequest.of(pageNumber, pageSize));
	}

	@Override
	public Slice<Board> getBoardsByUid(Long uid, Integer pageNumber, Integer pageSize) {
		return boardRepository.findAllByModeratorUid(uid, PageRequest.of(pageNumber, pageSize));
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
	public Board updateBoard(Long bid, String name, String description, Long uid, Long editorUid, UserRole userRole) {
		Board board = boardRepository.findById(bid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.board.not-found", null)));
		if (!board.getModerator().getUid().equals(editorUid)) {
			PermissionUtils.isNotRoleThenThrow(UserRole.ADMIN, userRole);
		}
		board.setName(name);
		board.setDescription(description);
		board.setModerator(userRepository.getReferenceById(uid));
		return board;
	}

	@Override
	public void deleteBoard(Long bid, Long editorUid, UserRole editorRole) {
		Board board = boardRepository.findById(bid).orElseThrow(
				() -> new IllegalArgumentException(MessageSourceUtils.getMessage("error.board.not-found", null)));
		if (!board.getModerator().getUid().equals(editorUid)) {
			PermissionUtils.isNotRoleThenThrow(UserRole.ADMIN, editorRole);
		}
		boardRepository.deleteById(bid);
	}
}
