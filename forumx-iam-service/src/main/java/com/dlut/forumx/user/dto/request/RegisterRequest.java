package com.dlut.forumx.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

	@NotBlank(message = "{validation.user.name.notblank}")
	@Size(min = 2, max = 10, message = "{validation.user.name.size}")
	@Pattern(regexp = "^[-0-9a-zA-Z_\\u4e00-\\u9fff]+$", message = "{validation.user.name.format}")
	private String username;

	@NotBlank(message = "{validation.user.email.notblank}")
	@Email(message = "{validation.user.email.format}")
	private String email;

	@NotBlank(message = "{validation.user.pwd.notblank}")
	@Size(min = 8, max = 16, message = "{validation.user.pwd.size}")
	@Pattern(regexp = "^(?=.*[0-9].*)(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*[~!@#$%&*()_+=-].*)[0-9a-zA-Z~!@#$%&*()_+=-]+$", message = "{validation.user.pwd.format}")
	private String password;

	@NotBlank(message = "{validation.user.name.notblank}")
	@Size(min = 2, max = 10, message = "{validation.user.name.size}")
	@Pattern(regexp = "^[-0-9a-zA-Z_\\u4e00-\\u9fff]+$", message = "{validation.user.name.format}")
	private String nickname;

	@Pattern(regexp = "^1[3-9]\\d{9}$", message = "{validation.user.phone.format}")
	private String phonenbr;
}
