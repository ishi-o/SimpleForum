package com.dlut.simpleforum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.dlut.simpleforum.dto.request.BoardCreateRequest;
import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.BoardDto;
import com.dlut.simpleforum.entity.Board;
import com.dlut.simpleforum.entity.User.UserRole;
import com.dlut.simpleforum.service.BoardService;
import com.dlut.simpleforum.service.PostService;
import com.dlut.simpleforum.util.PermissionUtils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * @author Ishi_O
 * @since
 */
@RestController
@RequestMapping("/boards")
public class BoardController {
	private final BoardService boardService;
	private final PostService postService;

	public BoardController(BoardService boardService, PostService postService) {
		this.boardService = boardService;
		this.postService = postService;
	}

	@GetMapping
	public ApiResponse<List<BoardDto>> getAllBoards(
			@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "10") @Positive Integer pageSize,
			@RequestParam(name = "q", required = false) String question) {
		List<Board> boards;
		if (question != null) {
			boards = boardService.getLikelyBoards(List.of(question.split(" ")), pageNumber, pageSize)
					.getContent();
		} else {
			boards = boardService.getAllBoards(pageNumber, pageSize).getContent();
		}
		return ApiResponse.success(boards.stream().map(
				board -> BoardDto.createBoardDto(board))
				.toList());
	}

	@GetMapping("/{bid}")
	public ApiResponse<BoardDto> getSpecifiedBoard(@PathVariable Long bid) {
		Board board = boardService.getSpecifiedBoard(bid);
		return ApiResponse.success(BoardDto.createBoardDto(board));
	}

	@PostMapping
	public ApiResponse<BoardDto> createBoard(
			@Valid @RequestBody BoardCreateRequest boardCreateRequest,
			@SessionAttribute("userId") Long uid,
			@SessionAttribute("userRole") UserRole role) {
		Long moderatorUid = boardCreateRequest.getModeratorUid();
		if (moderatorUid == null) {
			moderatorUid = uid;
		} else if (!moderatorUid.equals(uid)) {
			PermissionUtils.isNotRoleThenThrow(UserRole.ADMIN, role);
		}
		Board board = boardService.createBoard(boardCreateRequest.getName(),
				boardCreateRequest.getDescription(),
				boardCreateRequest.getModeratorUid() == null ? uid : boardCreateRequest.getModeratorUid());
		return ApiResponse.success(BoardDto.createBoardDto(board));
	}

	@PutMapping("/{bid}")
	public ApiResponse<BoardDto> updateBoard(
			@Valid @RequestBody BoardCreateRequest boardCreateRequest,
			@PathVariable Long bid,
			@SessionAttribute("userId") Long editorUid,
			@SessionAttribute("userRole") UserRole editorRole) {
		Board board = boardService.updateBoard(
				bid,
				boardCreateRequest.getName(),
				boardCreateRequest.getDescription(),
				boardCreateRequest.getModeratorUid(),
				editorUid,
				editorRole);
		return ApiResponse.success(BoardDto.createBoardDto(board));
	}

	@DeleteMapping("/{bid}")
	public ApiResponse<Void> deleteBoard(
			@PathVariable Long bid,
			@SessionAttribute("userId") Long editorUid,
			@SessionAttribute("userRole") UserRole editorRole) {
		boardService.deleteBoard(bid, editorUid, editorRole);
		return ApiResponse.success();
	}

	@PatchMapping("/{bid}/post-pin/{pid}")
	public ApiResponse<Void> trigglePin(
			@PathVariable Long bid,
			@PathVariable Long pid,
			@SessionAttribute("userId") Long uid,
			@SessionAttribute("userRole") UserRole userRole) {
		postService.trigglePin(bid, pid, uid, userRole);
		return ApiResponse.success();
	}
}
