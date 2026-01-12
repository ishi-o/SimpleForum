package com.dlut.forumx.content.service;

import com.dlut.forumx.content.entity.User;

public interface UserService {
	User getUser(Long uid);

	void register(String username, String password);

	User login(String username, String password);

	User logout(Long uid);
}
