package com.dlut.forumx.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dlut.forumx.user.entity.UserTag;

@Mapper
public interface UserTagMapper {
	int insert(UserTag tag);

	int updateWeight(UserTag tag);

	int delete(@Param("userId") Long userId, @Param("tagName") String tagName);

	int deleteByUser(@Param("userId") Long userId);

	List<UserTag> selectByUser(@Param("userId") Long userId);

	List<String> selectTagNamesByUser(@Param("userId") Long userId);

	List<UserTag> selectByTag(@Param("tagName") String tagName, @Param("limit") int limit);

	int batchInsert(@Param("list") List<UserTag> list);
}
