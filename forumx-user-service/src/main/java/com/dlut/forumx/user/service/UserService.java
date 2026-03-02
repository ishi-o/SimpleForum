package com.dlut.forumx.user.service;

import java.util.List;

import com.dlut.forumx.user.entity.User;
import com.dlut.forumx.user.entity.UserStats;

public interface UserService {

	User getUserById(Long userId);

	User getUserByKeycloakId(String keycloakId);

	List<User> getUsersByIds(List<Long> userIds);

	void createUser(User user);

	void updateUser(User user);

	Long getUserIdByKeycloakId(String keycloakId);

	UserStats getUserStats(Long userId);
}
