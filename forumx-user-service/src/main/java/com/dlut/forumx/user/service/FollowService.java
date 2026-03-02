package com.dlut.forumx.user.service;

import java.util.List;
import java.util.Map;

import com.dlut.forumx.user.entity.UserFollow;
import com.github.pagehelper.PageInfo;

public interface FollowService {
	void follow(Long followerId, Long followedId);

	void unfollow(Long followerId, Long followedId);

	boolean isFollowing(Long followerId, Long followedId);

	Map<Long, Boolean> batchCheckFollowing(Long followerId, List<Long> targetIds);

	PageInfo<UserFollow> getFollowers(Long userId, int pageNum, int pageSize);

	PageInfo<UserFollow> getFollowing(Long userId, int pageNum, int pageSize);

	int getFollowerCount(Long userId);

	int getFollowingCount(Long userId);

	List<Long> getFollowerIds(Long userId);

	List<Long> getFollowingIds(Long userId);
}
