package com.dlut.forumx.user.service;

import com.dlut.forumx.user.entity.UserSettings;

public interface SettingsService {
	UserSettings getSettings(Long userId);

	void updateSettings(UserSettings settings);
}
