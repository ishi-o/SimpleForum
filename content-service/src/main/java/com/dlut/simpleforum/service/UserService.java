package com.dlut.simpleforum.service;

import com.dlut.simpleforum.entity.User;

public interface UserService {
	User getUser(Long uid);

	void register(String username, String password);

	User login(String username, String password);

	User logout(Long uid);
}
