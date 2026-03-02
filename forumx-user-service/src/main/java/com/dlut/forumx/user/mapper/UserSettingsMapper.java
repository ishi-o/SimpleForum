package com.dlut.forumx.user.mapper;

import com.dlut.forumx.user.entity.UserSettings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserSettingsMapper {
	int insert(UserSettings settings);

	int update(UserSettings settings);

	UserSettings selectByUserId(@Param("userId") Long userId);

	int initSettings(@Param("userId") Long userId);
}
