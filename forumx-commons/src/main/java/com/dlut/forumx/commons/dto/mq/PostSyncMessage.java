package com.dlut.forumx.commons.dto.mq;

import java.io.Serializable;

import lombok.Data;

@Data
public class PostSyncMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String action; // CREATE, UPDATE, DELETE
	private Long postId;
	private Long timestamp;
}
