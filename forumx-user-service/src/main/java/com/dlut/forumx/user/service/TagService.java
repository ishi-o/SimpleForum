package com.dlut.forumx.user.service;

import java.util.List;

import com.dlut.forumx.user.entity.HotTag;
import com.dlut.forumx.user.entity.UserTag;
import com.github.pagehelper.PageInfo;

public interface TagService {
	List<UserTag> getUserTags(Long userId);

	void updateUserTags(Long userId, List<String> tagNames);

	PageInfo<HotTag> getHotTags(int pageNum, int pageSize);

	void incrTagUserCount(String tagName);

	void incrTagPostCount(String tagName);

	List<Long> getUserIdsByTag(String tagName, int limit);
}
