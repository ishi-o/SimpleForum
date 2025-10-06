package com.dlut.simpleforum.service;

import com.dlut.simpleforum.dto.result.UserLoginResult;

public interface UserService {
	void register(String username, String password);

	UserLoginResult login(String username, String password);
}
