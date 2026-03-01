package com.dlut.forumx.content.entity.enums;

import lombok.Getter;

@Getter
public enum FileType {
	IMAGE(1, "图片"),
	VIDEO(2, "视频"),
	AUDIO(3, "音频"),
	DOCUMENT(4, "文档");

	private final Integer code;
	private final String desc;

	FileType(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static FileType getByCode(Integer code) {
		for (FileType value : values()) {
			if (value.getCode().equals(code)) {
				return value;
			}
		}
		return null;
	}
}
