package com.dlut.simpleforum.dto.response;

import com.dlut.simpleforum.dto.result.UserLoginResult;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {
	private UserLoginResult userLoginResult;
}
