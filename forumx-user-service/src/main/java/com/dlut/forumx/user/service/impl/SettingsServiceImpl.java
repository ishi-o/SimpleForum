package com.dlut.forumx.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlut.forumx.user.entity.UserSettings;
import com.dlut.forumx.user.mapper.UserSettingsMapper;
import com.dlut.forumx.user.service.SettingsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

	private final UserSettingsMapper settingsMapper;
	// 用户设置变更不频繁，但不缓存因为数据量小

	@Override
	public UserSettings getSettings(Long userId) {
		UserSettings settings = settingsMapper.selectByUserId(userId);
		if (settings == null) {
			settingsMapper.initSettings(userId);
			settings = settingsMapper.selectByUserId(userId);
		}
		return settings;
	}

	@Override
	@Transactional
	public void updateSettings(UserSettings settings) {
		settingsMapper.update(settings);
	}
}
