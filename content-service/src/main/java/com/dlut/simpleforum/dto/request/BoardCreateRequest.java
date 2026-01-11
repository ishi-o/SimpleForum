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
public class BoardCreateRequest {
	@NotNull
	@Size(min = 2, max = 16, message = "{validation.board.name.size}")
	@Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fff]+$", message = "{validation.board.name.pattern}")
	private String name;

	@NotNull
	@Size(max = 40, message = "{validation.board.description.size}")
	private String description;

	@NotNull
	private Long moderatorUid;
}
