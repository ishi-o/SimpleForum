package com.dlut.forumx.commons.dto.mq;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserSyncMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String action; // CREATE, UPDATE, DELETE
	private Long userId;
	private Long timestamp;
}
