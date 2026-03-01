package com.dlut.forumx.content.entity.enums;

import lombok.Getter;

/**
 * 评论状态枚举
 */
@Getter
public enum CommentStatus {
	NORMAL(1, "正常"),
	HIDDEN(2, "隐藏"),
	DELETED(3, "删除");

	private final Integer code;
	private final String desc;

	CommentStatus(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static CommentStatus getByCode(Integer code) {
		for (CommentStatus value : values()) {
			if (value.getCode().equals(code)) {
				return value;
			}
		}
		return null;
	}
}
