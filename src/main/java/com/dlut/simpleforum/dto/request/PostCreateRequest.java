package com.dlut.simpleforum.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Ishi_O
 * @since
 */
@Data
public class PostCreateRequest {

	@NotNull
	@Size(min = 2, max = 10, message = "{validation.post.title.size}")
	@Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fff]+$", message = "{validation.post.title.pattern}")
	private String title;

	@NotNull
	@Size(max = 4096, message = "{validation.post.content.size}")
	private String content;
}
