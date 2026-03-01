package com.dlut.forumx.content.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Comment {
	private Long cid;
	private String content;
	private Long authorId;
	private Long postId;
	private Long parentId;
	private Long replyToUserId;
	private Integer status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Post post;
	private Comment parentComment;
	private List<Comment> replies;
}
