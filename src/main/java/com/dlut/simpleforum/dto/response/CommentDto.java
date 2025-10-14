package com.dlut.simpleforum.dto.response;

import java.time.LocalDateTime;

import com.dlut.simpleforum.entity.Comment;
import com.dlut.simpleforum.entity.SubComment;

import lombok.Builder;
import lombok.Data;

/**
 * @author Ishi_O
 * @since
 */
@Data
@Builder
public class CommentDto {
	private Long cid;

	private String content;

	private Long authorId;

	private LocalDateTime createdAt;

	private Integer likes;

	private Integer dislikes;

	private Long targetId;

	public static CommentDto createMainCommentDto(Comment comment) {
		return builder()
				.cid(comment.getCid())
				.authorId(comment.getAuthor().getUid())
				.content(comment.getContent())
				.createdAt(comment.getCreatedAt())
				.likes(comment.getLikes())
				.dislikes(comment.getDislikes())
				.targetId(null)
				.build();
	}

	public static CommentDto createSubCommentDto(SubComment subComment) {
		return builder()
				.cid(subComment.getCid())
				.authorId(subComment.getAuthor().getUid())
				.content(subComment.getContent())
				.createdAt(subComment.getCreatedAt())
				.likes(subComment.getLikes())
				.dislikes(subComment.getDislikes())
				.targetId(subComment.getTarget() == null ? null : subComment.getTarget().getCid())
				.build();
	}
}
