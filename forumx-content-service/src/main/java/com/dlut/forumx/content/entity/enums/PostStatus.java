package com.dlut.forumx.content.entity.enums;

import lombok.Getter;

@Getter
public enum PostStatus {
	PUBLISHED(1, "已发布"),
	DRAFT(2, "草稿"),
	REVIEWING(3, "审核中"),
	HIDDEN(4, "隐藏");

	private final Integer code;
	private final String desc;

	PostStatus(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PostStatus getByCode(Integer code) {
		for (PostStatus value : values()) {
			if (value.getCode().equals(code)) {
				return value;
			}
		}
		return null;
	}
}
