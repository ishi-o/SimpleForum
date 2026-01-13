package com.dlut.forumx.content.dto.response;

import java.time.LocalDateTime;

import com.dlut.forumx.content.entity.Comment;
import com.dlut.forumx.content.entity.SubComment;
import com.dlut.forumx.content.entity.User;

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

	private String authorName;

	private LocalDateTime createdAt;

	private Integer likes;

	private Integer dislikes;

	private Long targetId;

	private Long targetAuthorId;

	private String targetAuthorName;

	public static CommentDto createMainCommentDto(Comment comment) {
		return builder()
				.cid(comment.getCid())
				.authorId(comment.getAuthor().getUid())
				.authorName(comment.getAuthor().getName())
				.content(comment.getContent())
				.createdAt(comment.getCreatedAt())
				.likes(comment.getLikes())
				.dislikes(comment.getDislikes())
				.targetId(null)
				.targetAuthorId(null)
				.targetAuthorName(null)
				.build();
	}

	public static CommentDto createSubCommentDto(SubComment subComment) {
		SubComment target = subComment.getTarget();
		Long targetId, targetAuthorId = targetId = null;
		String targetAuthorName = null;
		if (target != null) {
			targetId = target.getCid();
			User author = target.getAuthor();
			targetAuthorId = author.getUid();
			targetAuthorName = author.getName();
		}
		return builder()
				.cid(subComment.getCid())
				.authorId(subComment.getAuthor().getUid())
				.authorName(subComment.getAuthor().getName())
				.content(subComment.getContent())
				.createdAt(subComment.getCreatedAt())
				.likes(subComment.getLikes())
				.dislikes(subComment.getDislikes())
				.targetId(targetId)
				.targetAuthorId(targetAuthorId)
				.targetAuthorName(targetAuthorName)
				.build();
	}
}
