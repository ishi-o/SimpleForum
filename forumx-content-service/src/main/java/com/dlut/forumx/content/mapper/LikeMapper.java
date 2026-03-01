package com.dlut.forumx.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.content.entity.Like;

@Mapper
public interface LikeMapper {
	int insert(Like like);

	int deleteById(@Param("lid") Long lid);

	int deleteByUserAndTarget(@Param("userId") String userId,
			@Param("targetType") Integer targetType,
			@Param("targetId") Long targetId);

	Like selectById(@Param("lid") Long lid);

	Like selectByUserAndTarget(@Param("userId") String userId,
			@Param("targetType") Integer targetType,
			@Param("targetId") Long targetId);

	boolean exists(@Param("userId") String userId,
			@Param("targetType") Integer targetType,
			@Param("targetId") Long targetId);

	long countByTarget(@Param("targetType") Integer targetType,
			@Param("targetId") Long targetId);

	List<Long> selectLikedPostIdsByUser(@Param("userId") String userId,
			@Param("offset") int offset,
			@Param("limit") int limit);

	List<Long> selectLikedCommentIdsByUser(@Param("userId") String userId,
			@Param("offset") int offset,
			@Param("limit") int limit);
}
