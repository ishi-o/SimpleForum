package com.dlut.simpleforum.service;

import com.dlut.simpleforum.entity.User;

/**
 * @author Ishi_O
 * @since
 */
public interface UserService {
	void register(String username, String password);

	User getUser(Long uid);
}
