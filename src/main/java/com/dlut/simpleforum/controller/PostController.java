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

import com.dlut.simpleforum.dto.request.PostCreateRequest;
import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.PostDto;
import com.dlut.simpleforum.dto.result.PageResult;
import com.dlut.simpleforum.entity.Post;
import com.dlut.simpleforum.service.PostService;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * @author Ishi_O
 * @since
 */
@RestController
@RequestMapping("/boards/{bid}/posts")
public class PostController {
	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping
	public ApiResponse<List<PostDto>> getAllPosts(
			@PathVariable Long bid,
			@RequestParam(name = "page", defaultValue = "0") @PositiveOrZero Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "10") @Positive Integer pageSize) {
		PageResult<Post> posts = postService.getAllPostsByBoardId(bid, pageNumber, pageSize);
		return ApiResponse.success(posts
				.getContent()
				.stream()
				.map(post -> PostDto.createPostDto(post))
				.toList());
	}

	@GetMapping("/{pid}")
	public ApiResponse<PostDto> getSpecifiedPost(
			@PathVariable Long pid,
			@PathVariable Long bid) {
		Post post = postService.getSpecifiedPostByBoardId(pid, bid);
		return ApiResponse.success(PostDto.createPostDto(post));
	}

	@PostMapping
	public ApiResponse<PostDto> createPost(
			@RequestBody PostCreateRequest postCreateRequest,
			@PathVariable Long bid,
			@SessionAttribute("userId") Long uid) {
		Post post = postService.createPostByBoardId(bid, uid, postCreateRequest.getTitle(),
				postCreateRequest.getContent());
		return ApiResponse.success(PostDto.createPostDto(post));
	}

}
