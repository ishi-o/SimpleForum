package com.dlut.simpleforum.service;

import com.dlut.simpleforum.entity.User;

public interface UserService {
	void register(String username, String password);

	User login(String username, String password);

	void logout(Long uid);
}
