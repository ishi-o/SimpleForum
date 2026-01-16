package com.dlut.forumx.user.dto.request;

import com.dlut.forumx.common.api.ValidationGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Request: Ask for send verification code
 */
@Data
public class VerificationSendRequest {

	@Email(message = "{validation.user.email.format}", groups = ValidationGroup.EmailGroup.class)
	@Pattern(regexp = "^1[3-9]\\d{9}$", message = "{validation.user.phone.format}", groups = ValidationGroup.PhoneGroup.class)
	private String target;

}
