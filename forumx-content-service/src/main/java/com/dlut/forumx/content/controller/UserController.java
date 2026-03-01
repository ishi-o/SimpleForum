package com.dlut.forumx.content.controller;

import java.util.List;

import org.apache.seata.common.result.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.forumx.common.content.dto.response.BoardDto;
import com.dlut.forumx.common.dto.ApiResponse;
import com.dlut.forumx.content.entity.Board;
import com.dlut.forumx.content.service.BoardService;
import com.dlut.forumx.content.service.UserService;

/**
 * @author Ishi_O
 * @since
 */
@RestController
@RequestMapping("/users/{uid}")
public class UserController {
	private final UserService userService;
	private final BoardService boardService;

	public UserController(UserService userService, BoardService boardService) {
		this.userService = userService;
		this.boardService = boardService;
	}

	@GetMapping
	public ApiResponse<UserDto> getUserProfile(@PathVariable Long uid) {
		return ApiResponse.success(UserDto.createUserDtoByUser(userService.getUser(uid)));
	}

	@GetMapping("/boards")
	public ApiResponse<List<BoardDto>> getBoards(@PathVariable Long uid,
			@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "10") Integer pageSize) {
		PageResult<Board> boards = boardService.getBoardsByUid(uid, pageNumber, pageSize);
		return ApiResponse.success(boards
				.getContent()
				.stream()
				.map((board) -> BoardDto.createBoardDto(board))
				.toList());
	}
}
