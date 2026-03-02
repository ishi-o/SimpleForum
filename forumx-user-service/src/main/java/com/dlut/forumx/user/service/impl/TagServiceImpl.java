package com.dlut.forumx.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlut.forumx.user.entity.HotTag;
import com.dlut.forumx.user.entity.UserTag;
import com.dlut.forumx.user.mapper.HotTagMapper;
import com.dlut.forumx.user.mapper.UserTagMapper;
import com.dlut.forumx.user.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

	private final UserTagMapper userTagMapper;
	private final HotTagMapper hotTagMapper;
	// 标签数据变更频繁，不缓存

	@Override
	public List<UserTag> getUserTags(Long userId) {
		return userTagMapper.selectByUser(userId);
	}

	@Override
	@Transactional
	public void updateUserTags(Long userId, List<String> tagNames) {
		userTagMapper.deleteByUser(userId);

		if (tagNames != null && !tagNames.isEmpty()) {
			List<UserTag> tags = new ArrayList<>();
			for (String tagName : tagNames) {
				UserTag tag = new UserTag();
				tag.setUserId(userId);
				tag.setTagName(tagName);
				tag.setTagSource(1);
				tag.setWeight(1);
				tags.add(tag);
				incrTagUserCount(tagName);
			}
			userTagMapper.batchInsert(tags);
		}
	}

	@Override
	public PageInfo<HotTag> getHotTags(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<HotTag> hotTags = hotTagMapper.selectHotTags(Integer.MAX_VALUE);
		return new PageInfo<>(hotTags);
	}

	@Override
	@Transactional
	public void incrTagUserCount(String tagName) {
		HotTag hotTag = hotTagMapper.selectByTagName(tagName);
		if (hotTag == null) {
			hotTag = new HotTag();
			hotTag.setTagName(tagName);
			hotTag.setUserCount(1);
			hotTag.setPostCount(0);
			hotTag.setWeight(1);
			hotTagMapper.insert(hotTag);
		} else {
			hotTagMapper.incrUserCount(tagName);
		}
	}

	@Override
	@Transactional
	public void incrTagPostCount(String tagName) {
		HotTag hotTag = hotTagMapper.selectByTagName(tagName);
		if (hotTag == null) {
			hotTag = new HotTag();
			hotTag.setTagName(tagName);
			hotTag.setUserCount(0);
			hotTag.setPostCount(1);
			hotTag.setWeight(1);
			hotTagMapper.insert(hotTag);
		} else {
			hotTagMapper.incrPostCount(tagName);
		}
	}

	@Override
	public List<Long> getUserIdsByTag(String tagName, int limit) {
		List<UserTag> userTags = userTagMapper.selectByTag(tagName, limit);
		return userTags.stream()
				.map(UserTag::getUserId)
				.collect(Collectors.toList());
	}
}
