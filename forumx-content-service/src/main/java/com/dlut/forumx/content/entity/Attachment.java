package com.dlut.forumx.content.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Attachment {
	private Long aid;
	private Long postId;
	private String objectKey;
	private String originalName;
	private Integer fileSize;
	private String mimeType;
	private Integer fileType;
	private Integer width;
	private Integer height;
	private Integer sortOrder;
	private LocalDateTime createdAt;
	private Post post;
}
