package com.dlut.forumx.content.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Post {
	private Long pid;
	private String title;
	private Map<String, Object> content;
	private Long authorId;
	private Long boardId;
	private Integer status;
	private Integer viewCount;
	private Boolean isPinned;
	private Boolean isEssence;
	private List<String> tags;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Board board;
	private List<Comment> comments;
	private List<Attachment> attachments;
}
