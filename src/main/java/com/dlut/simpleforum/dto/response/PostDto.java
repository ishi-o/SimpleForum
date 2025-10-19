package com.dlut.simpleforum.dto.response;

import java.time.LocalDateTime;

import com.dlut.simpleforum.entity.Post;

import lombok.Builder;
import lombok.Data;

/**
 * @author Ishi_O
 * @since
 */
@Data
@Builder
public class PostDto {
	private Long pid;

	private String title;

	private String content;

	private LocalDateTime createdAt;

	private Long bid;

	private Long authorId;

	private String authorName;

	private Boolean isPinned;

	private Integer likes;

	private Integer dislikes;

	public static PostDto createPostDto(Post post) {
		return builder()
				.pid(post.getPid())
				.title(post.getTitle())
				.content(post.getContent())
				.createdAt(post.getCreatedAt())
				.bid(post.getBoard().getBid())
				.authorId(post.getAuthor().getUid())
				.authorName(post.getAuthor().getName())
				.isPinned(post.getIsPinned())
				.likes(post.getLikes())
				.dislikes(post.getDislikes())
				.build();
	}
}
