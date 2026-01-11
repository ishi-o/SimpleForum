package com.dlut.simpleforum.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserAuthRequest {

	@NotNull
	@Size(min = 2, max = 10, message = "{validation.user.name.size}")
	@Pattern(regexp = "^[0-9a-zA-Z_\\u4e00-\\u9fff]+$", message = "{validation.user.name.pattern}")
	String username;

	@NotNull
	@Size(min = 8, max = 16, message = "{validation.user.pwd.size}")
	@Pattern(regexp = "^(?=.*[0-9].*)(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*[~!@#$%&*()_+=-].*)[0-9a-zA-Z~!@#$%&*()_+=-]+$", message = "{validation.user.pwd.pattern}")
	String password;
}
