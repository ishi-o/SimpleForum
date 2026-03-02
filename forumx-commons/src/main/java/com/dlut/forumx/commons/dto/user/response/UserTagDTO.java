package com.dlut.forumx.commons.dto.user.response;

import lombok.Data;

@Data
public class UserTagDTO {
	private String tagName;
	private Integer weight;
	private Integer source; // tagSource字段映射为source
}
