package com.dlut.simpleforum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.CommentDto;
import com.dlut.simpleforum.service.CommentService;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * @author Ishi_O
 * @since
 */
@RestController
@RequestMapping("/boards/{bid}/posts/{pid}/comments")
public class CommentController {
	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping
	public ApiResponse<List<CommentDto>> getAllComments(
			@PathVariable Long bid,
			@PathVariable Long pid,
			@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "10") @Positive Integer pageSize) {
		return ApiResponse.success(commentService
				.getAllMainComments(bid, pid, pageNumber, pageSize)
				.getContent()
				.stream()
				.map(comment -> CommentDto.createMainCommentDto(comment))
				.toList());
	}

	@GetMapping("/{cid}")
	public ApiResponse<List<CommentDto>> getSpecifiedComments(
			@PathVariable Long pid,
			@PathVariable Long bid,
			@PathVariable Long cid,
			@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "10") @Positive Integer pageSize) {
		return ApiResponse.success(commentService
				.getSpecifiedSubComments(bid, pid, cid, pageNumber, pageSize)
				.getContent()
				.stream()
				.map(subComment -> CommentDto.createSubCommentDto(subComment))
				.toList());
	}

}
