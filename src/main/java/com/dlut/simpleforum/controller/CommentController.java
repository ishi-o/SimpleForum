package com.dlut.simpleforum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.CommentDto;
import com.dlut.simpleforum.dto.result.PageResult;
import com.dlut.simpleforum.service.CommentService;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

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
	public ApiResponse<PageResult<CommentDto>> getSpecifiedComments(
			@PathVariable Long bid,
			@PathVariable Long pid,
			@PathVariable Long cid,
			@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "10") @Positive Integer pageSize) {
		return ApiResponse.success(PageResult.from(
				commentService.getSpecifiedSubComments(bid, pid, cid, pageNumber, pageSize),
				(cmt) -> CommentDto.createSubCommentDto(cmt)));
	}

	@PostMapping
	public ApiResponse<CommentDto> createMainComment(
			@PathVariable Long pid,
			@SessionAttribute("userId") Long uid,
			@RequestBody @Size(min = 1, max = 128, message = "{validation.comment.content.size}") String content) {
		return ApiResponse.success(CommentDto.createMainCommentDto(
				commentService.createMainComment(pid, uid, content)));
	}

	@PostMapping("/{cid}")
	public ApiResponse<CommentDto> createSubComment(
			@PathVariable Long pid,
			@PathVariable Long cid,
			@SessionAttribute("userId") Long uid,
			@RequestBody @Size(min = 1, max = 128, message = "{validation.comment.content.size}") String content) {
		return ApiResponse.success(CommentDto.createSubCommentDto(
				commentService.createSubComment(pid, cid, uid, null, content)));
	}

	@PostMapping("/{cid}/replies/{targetId}")
	public ApiResponse<CommentDto> createReply(
			@PathVariable Long bid,
			@PathVariable Long pid,
			@PathVariable Long cid,
			@PathVariable Long targetId,
			@SessionAttribute("userId") Long uid,
			@RequestBody @Size(min = 1, max = 128, message = "{validation.comment.content.size}") String content) {
		return ApiResponse.success(CommentDto.createSubCommentDto(
				commentService.createSubComment(pid, cid, uid, targetId, content)));
	}

}
