package com.dlut.forumx.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.user.entity.UserFollow;

@Mapper
public interface UserFollowMapper {
	int insert(UserFollow follow);

	int delete(@Param("followerId") Long followerId, @Param("followedId") Long followedId);

	UserFollow select(@Param("followerId") Long followerId, @Param("followedId") Long followedId);

	List<Long> selectFollowers(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

	List<Long> selectFollowing(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

	int countFollowers(@Param("userId") Long userId);

	int countFollowing(@Param("userId") Long userId);

	List<UserFollow> selectFollowRelations(@Param("userId") Long userId, @Param("targetIds") List<Long> targetIds);

	List<UserFollow> selectByFollowedId(@Param("followedId") Long followedId);

	List<UserFollow> selectByFollowerId(@Param("followerId") Long followerId);
}
