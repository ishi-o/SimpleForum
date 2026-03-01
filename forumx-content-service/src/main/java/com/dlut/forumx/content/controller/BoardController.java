package com.dlut.forumx.content.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.forumx.common.content.dto.request.BoardCreateRequest;
import com.dlut.forumx.common.content.dto.response.BoardDto;
import com.dlut.forumx.common.dto.ApiResponse;
import com.dlut.forumx.content.entity.Board;
import com.dlut.forumx.content.service.BoardService;
import com.dlut.forumx.content.service.PostService;

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
			@RequestParam(name = "q", required = false) String question,
			@RequestHeader("X-User-Id") String userId,
			@RequestHeader("X-Username") String username,
			@RequestHeader("X-User-Roles") String rolesStr) {

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
	public ApiResponse<BoardDto> getSpecifiedBoard(
			@PathVariable Long bid,
			@RequestHeader("X-User-Id") String userId) {

		Board board = boardService.getSpecifiedBoard(bid);
		return ApiResponse.success(BoardDto.createBoardDto(board));
	}

	@PostMapping
	public ApiResponse<BoardDto> createBoard(
			@Valid @RequestBody BoardCreateRequest boardCreateRequest,
			@RequestHeader("X-User-Id") String userId,
			@RequestHeader("X-User-Roles") String rolesStr) {

		List<String> roles = Arrays.asList(rolesStr.split(","));
		String moderatorId = boardCreateRequest.getModeratorId();

		if (moderatorId == null) {
			moderatorId = userId;
		} else if (!moderatorId.equals(userId) && !roles.contains("ADMIN")) {
			throw new PermissionDeniedException("只有管理员可以指定其他版主");
		}

		Board board = boardService.createBoard(
				boardCreateRequest.getName(),
				boardCreateRequest.getDescription(),
				moderatorId);

		return ApiResponse.success(BoardDto.createBoardDto(board));
	}

	@PutMapping("/{bid}")
	public ApiResponse<BoardDto> updateBoard(
			@Valid @RequestBody BoardCreateRequest boardCreateRequest,
			@PathVariable Long bid,
			@RequestHeader("X-User-Id") String userId,
			@RequestHeader("X-User-Roles") String rolesStr) {

		List<String> roles = Arrays.asList(rolesStr.split(","));

		Board board = boardService.updateBoard(
				bid,
				boardCreateRequest.getName(),
				boardCreateRequest.getDescription(),
				boardCreateRequest.getModeratorId(),
				userId,
				roles);

		return ApiResponse.success(BoardDto.createBoardDto(board));
	}

	@DeleteMapping("/{bid}")
	public ApiResponse<Void> deleteBoard(
			@PathVariable Long bid,
			@RequestHeader("X-User-Id") String userId,
			@RequestHeader("X-User-Roles") String rolesStr) {

		List<String> roles = Arrays.asList(rolesStr.split(","));

		boardService.deleteBoard(bid, userId, roles);
		return ApiResponse.success();
	}

	@PatchMapping("/{bid}/post-pin/{pid}")
	public ApiResponse<Void> trigglePin(
			@PathVariable Long bid,
			@PathVariable Long pid,
			@RequestHeader("X-User-Id") String userId,
			@RequestHeader("X-User-Roles") String rolesStr) {

		List<String> roles = Arrays.asList(rolesStr.split(","));

		postService.trigglePin(bid, pid, userId, roles);
		return ApiResponse.success();
	}
}
