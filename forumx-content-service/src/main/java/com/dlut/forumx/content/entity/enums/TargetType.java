package com.dlut.forumx.content.entity.enums;

import lombok.Getter;

@Getter
public enum TargetType {
	POST(1, "帖子"),
	COMMENT(2, "评论");

	private final Integer code;
	private final String desc;

	TargetType(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static TargetType getByCode(Integer code) {
		for (TargetType value : values()) {
			if (value.getCode().equals(code)) {
				return value;
			}
		}
		return null;
	}
}
