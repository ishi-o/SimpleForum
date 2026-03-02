package com.dlut.forumx.commons.dto.user.request;

import java.util.List;

import lombok.Data;

@Data
public class UpdateTagsRequestDTO {
	private List<String> tags;
}
