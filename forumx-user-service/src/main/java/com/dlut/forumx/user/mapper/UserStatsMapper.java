package com.dlut.forumx.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.user.entity.UserStats;

@Mapper
public interface UserStatsMapper {
	int insert(UserStats stats);

	int update(UserStats stats);

	UserStats selectByUserId(@Param("userId") Long userId);

	int initStats(@Param("userId") Long userId);

	int incrFollowCount(@Param("userId") Long userId, @Param("delta") int delta);

	int incrFansCount(@Param("userId") Long userId, @Param("delta") int delta);

	int incrPostCount(@Param("userId") Long userId);

	int incrCommentCount(@Param("userId") Long userId);

	int incrLikeCount(@Param("userId") Long userId);

	int incrFavoriteCount(@Param("userId") Long userId);

	int incrViewCount(@Param("userId") Long userId);
}
